package gdgjobthon

import (
	"encoding/json"
	"errors"
	"log"
	"net/http"
	"strconv"
	"strings"

	"appengine"
	"appengine/datastore"
	"appengine/taskqueue"

	"github.com/go-martini/martini"
	"github.com/martini-contrib/render"
)

type Analise struct {
	Id              int64   `json:"id" datastore:"-"`
	EmpresaId       string  `json:"eid"`
	VagaId          int64   `json:"vid"`
	CurriculoId     string  `json:"cid"`
	Compatibilidade float64 `json:"compatibilidade"`
	EmpresaNome     string  `json:"nomeEmpresa"`
	VagaTitulo      string  `json:"nomeVaga"`
	CurriculoNome   string  `json:"nomeCurriculo"`
}

type Curriculo struct {
	Email       string   `json:"email"`
	Nome        string   `json:"nome"`
	Idade       int64    `json:"idade"`
	Habilidades []string `json:"habilidades"`
	Cidade      string   `json:"cidade"`
	Estado      string   `json:"estado"`
	Telefone    string   `json:"telefone"`
}

type Empresa struct {
	Email    string `json:"email"`
	Telefone string `json:"telefone"`
	Nome     string `json:"nome"`
	Cidade   string `json:"cidade"`
	Estado   string `json:"estado"`
	Sobre    string `json:"sobre"`
}

type Vaga struct {
	Id          int64    `json:"id" datastore:"-"`
	Email       string   `json:"email"`
	Titulo      string   `json:"titulo"`
	Sobre       string   `json:"sobre"`
	Habilidades []string `json:"habilidades"`
	Area        string   `json:"area"`
	Cidade      string   `json:"cidade"`
	Estado      string   `json:"estado"`
	Ativa       bool     `json:"ativa"`
	Contratacao []string `json:"contratacao"`
}

func appEngine(c martini.Context, r *http.Request) {
	c.Map(appengine.NewContext(r))

}

func cORS(w http.ResponseWriter) {
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS")
	w.Header().Set("Access-Control-Allow-Headers", "Accept, Content-Type, Content-Length, Accept-Encoding, X-Requested-With, X-CSRF-Token")
	w.Header().Set("Access-Control-Allow-Credentials", "true")
}

func init() {
	m := martini.Classic()
	m.Use(render.Renderer())
	m.Use(appEngine)
	m.Use(cORS)

	m.Get("/empresas", listEmpresas)
	m.Get("/empresa/:email", getEmpresa)
	m.Post("/empresa", addEmpresa)

	m.Get("/vagas", listVagas)
	m.Get("/vagas/:email", getVagas)
	m.Get("/vaga/:id", getVaga)
	m.Post("/vaga", addVaga)

	m.Get("/curriculos", listCurriculos)
	m.Get("/curriculo/:email", getCurriculo)
	m.Post("/curriculo", addCurriculo)

	m.Get("/analises/vaga/:vagaId", listAnaliseVaga)
	m.Get("/analises/curriculo/:curriculoEmail", listAnaliseCurriculo)

	// Task
	m.Post("/matcher/curriculos", matcherCurriculos)
	m.Post("/matcher/vagas", matcherVagas)

	http.Handle("/", m)
}

func listEmpresas(c appengine.Context, r render.Render, params martini.Params) {
	var empresas []Empresa
	q := datastore.NewQuery("Empresa")
	_, err := q.GetAll(c, &empresas)

	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	r.JSON(http.StatusOK, empresas)
}

func addEmpresa(c appengine.Context, r render.Render, req *http.Request) {
	empresa := new(Empresa)
	decoder := json.NewDecoder(req.Body)
	err := decoder.Decode(&empresa)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	key := datastore.NewKey(c, "Empresa", empresa.Email, 0, nil)
	_, err = datastore.Put(c, key, empresa)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	r.JSON(http.StatusOK, "success")
}

func getEmpresa(c appengine.Context, r render.Render, params martini.Params) {
	key := datastore.NewKey(c, "Empresa", params["email"], 0, nil)
	empresa := new(Empresa)
	err := datastore.Get(c, key, empresa)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	r.JSON(http.StatusOK, empresa)
}

func addVaga(c appengine.Context, r render.Render, req *http.Request) {
	// Entender como utilizar as ancestors para linkas as chaves com a empresa
	// Código abaixo com problemas
	vaga := new(Vaga)
	decoder := json.NewDecoder(req.Body)
	err := decoder.Decode(&vaga)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	q := datastore.NewQuery("Vaga").Filter("Email =", vaga.Email).Filter("Titulo =", vaga.Titulo)
	size, err := q.Count(c)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}
	if size > 0 {
		err = errors.New("job: já há uma vaga desta empresa cadastrada com este nome")
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	// key := datastore.NewKey(c, "Vaga", "", 0, nil)
	key := datastore.NewIncompleteKey(c, "Vaga", nil)
	realKey, err := datastore.Put(c, key, vaga)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	vagaId := strconv.Itoa(int(realKey.IntID()))
	t := taskqueue.NewPOSTTask("/matcher/vagas", map[string][]string{"vaga": {vagaId}})
	if _, err := taskqueue.Add(c, t, ""); err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	r.JSON(http.StatusOK, "success")
}

func getVaga(c appengine.Context, r render.Render, params martini.Params) {
	intKey, err := strconv.ParseInt(params["id"], 10, 64)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	key := datastore.NewKey(c, "Vaga", "", intKey, nil)
	vaga := new(Vaga)
	err = datastore.Get(c, key, vaga)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	vaga.Id = key.IntID()

	r.JSON(http.StatusOK, vaga)
}

func getVagas(c appengine.Context, r render.Render, params martini.Params) {
	var vagas []Vaga
	q := datastore.NewQuery("Vaga").Filter("Email =", params["email"])
	keys, err := q.GetAll(c, &vagas)

	for idx, _ := range vagas {
		vagas[idx].Id = keys[idx].IntID()
	}

	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	r.JSON(http.StatusOK, vagas)
}

func listVagas(c appengine.Context, r render.Render, params martini.Params) {
	var vagas []Vaga
	q := datastore.NewQuery("Vaga")
	keys, err := q.GetAll(c, &vagas)

	for idx, _ := range vagas {
		vagas[idx].Id = keys[idx].IntID()
	}

	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	r.JSON(http.StatusOK, vagas)
}

func addCurriculo(c appengine.Context, r render.Render, req *http.Request) {
	curriculo := new(Curriculo)
	decoder := json.NewDecoder(req.Body)
	err := decoder.Decode(&curriculo)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	key := datastore.NewKey(c, "Curriculo", curriculo.Email, 0, nil)
	_, err = datastore.Put(c, key, curriculo)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	t := taskqueue.NewPOSTTask("/matcher/curriculos", map[string][]string{
		"curriculo": {curriculo.Email},
	})
	if _, err := taskqueue.Add(c, t, ""); err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	r.JSON(http.StatusOK, "success")
}

func getCurriculo(c appengine.Context, r render.Render, params martini.Params) {
	key := datastore.NewKey(c, "Curriculo", params["email"], 0, nil)
	curriculo := new(Curriculo)
	err := datastore.Get(c, key, curriculo)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	r.JSON(http.StatusOK, curriculo)
}

func listCurriculos(c appengine.Context, r render.Render, params martini.Params) {
	var curriculos []Curriculo
	q := datastore.NewQuery("Curriculo")
	_, err := q.GetAll(c, &curriculos)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	r.JSON(http.StatusOK, curriculos)
}

// Matcher DesInteligente
func matcherCurriculos(c appengine.Context, r render.Render, req *http.Request) {
	curriculoId := req.FormValue("curriculo")
	key := datastore.NewKey(c, "Curriculo", curriculoId, 0, nil)
	curriculo := new(Curriculo)
	err := datastore.Get(c, key, curriculo)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	var vagas []Vaga
	q := datastore.NewQuery("Vaga")
	keys, err := q.GetAll(c, &vagas)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	for idx, vaga := range vagas {
		empresaKey := datastore.NewKey(c, "Empresa", vaga.Email, 0, nil)
		empresa := new(Empresa)
		err = datastore.Get(c, empresaKey, empresa)
		if err != nil {
			log.Println(err)
			r.JSON(http.StatusInternalServerError, err.Error())
			return
		}

		analise := &Analise{
			EmpresaId:     vaga.Email,
			VagaId:        keys[idx].IntID(),
			CurriculoId:   curriculo.Email,
			EmpresaNome:   empresa.Nome,
			VagaTitulo:    vaga.Titulo,
			CurriculoNome: curriculo.Nome,
		}

		necessidades := vaga.Habilidades
		capacidades := curriculo.Habilidades
		go matchSuperDesinteressante(necessidades, capacidades, analise, c)
	}

	r.JSON(http.StatusOK, "success")
}

func matcherVagas(c appengine.Context, r render.Render, req *http.Request) {
	log.Println(req.FormValue("vaga"))
	vagaId, err := strconv.ParseInt(req.FormValue("vaga"), 10, 64)
	if err != nil {
		// log.Println(err)
		// r.JSON(http.StatusInternalServerError, err.Error())
		return
	}
	vagaKey := datastore.NewKey(c, "Vaga", "", vagaId, nil)
	vaga := new(Vaga)
	err = datastore.Get(c, vagaKey, vaga)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	var curriculos []Curriculo
	q := datastore.NewQuery("Curriculo")
	_, err = q.GetAll(c, &curriculos)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	empresaKey := datastore.NewKey(c, "Empresa", vaga.Email, 0, nil)
	empresa := new(Empresa)
	err = datastore.Get(c, empresaKey, empresa)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	for _, curriculo := range curriculos {
		analise := &Analise{
			EmpresaId:     vaga.Email,
			VagaId:        vagaKey.IntID(),
			CurriculoId:   curriculo.Email,
			EmpresaNome:   empresa.Nome,
			VagaTitulo:    vaga.Titulo,
			CurriculoNome: curriculo.Nome,
		}

		necessidades := vaga.Habilidades
		capacidades := curriculo.Habilidades
		go matchSuperDesinteressante(necessidades, capacidades, analise, c)
	}

	r.JSON(http.StatusOK, "success")
}

func listAnaliseVaga(c appengine.Context, r render.Render, params martini.Params) {
	vagaId, err := strconv.ParseInt(params["vagaId"], 10, 64)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	var analises []Analise
	q := datastore.NewQuery("Analise").Filter("VagaId =", vagaId)
	_, err = q.GetAll(c, &analises)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	r.JSON(http.StatusOK, analises)
}

func listAnaliseCurriculo(c appengine.Context, r render.Render, params martini.Params) {
	var analise []Analise
	q := datastore.NewQuery("Analise").Filter("CurriculoId =", params["curriculoEmail"])
	_, err := q.GetAll(c, &analise)
	if err != nil {
		log.Println(err)
		r.JSON(http.StatusInternalServerError, err.Error())
		return
	}

	r.JSON(http.StatusOK, analise)
}

func (a *Analise) Salvar(c appengine.Context) error {
	uniqueKey := a.EmpresaId + "|" + a.CurriculoId + "|" + strconv.Itoa(int(a.VagaId))
	key := datastore.NewKey(c, "Analise", uniqueKey, 0, nil)
	_, err := datastore.Put(c, key, a)
	return err
}

func matchSuperDesinteressante(necessidades, capacidades []string, a *Analise, c appengine.Context) {
	totalNecessidades := float64(len(necessidades))
	capacidadesEncontradas := 0.0
	for _, necessidade := range necessidades {
		for _, capacidade := range capacidades {
			if strings.ToLower(necessidade) == strings.ToLower(capacidade) {
				capacidadesEncontradas = capacidadesEncontradas + 1.0
				continue
			}
		}
	}

	a.Compatibilidade = (capacidadesEncontradas / totalNecessidades) * 100.0
	if err := a.Salvar(c); err != nil {
		log.Println(err)
	}
}
