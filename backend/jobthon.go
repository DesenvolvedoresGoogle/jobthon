package gdgjobthon

import (
	"encoding/json"
	"errors"
	"log"
	"net/http"
	"strconv"

	"appengine"
	"appengine/datastore"

	"github.com/go-martini/martini"
	"github.com/martini-contrib/render"
)

type Analise struct {
	Id              int64 `json:"id" datastore:"-"`
	EmpresaId       datastore.Key
	VagaId          datastore.Key
	CurriculoId     datastore.Key
	Compatibilidade float64
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
	Id          int64    `json:"id"`
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
	datastore.Put(c, key, empresa)
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

	key := datastore.NewKey(c, "Vaga", "", 0, nil)
	datastore.Put(c, key, vaga)
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
	datastore.Put(c, key, curriculo)
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
