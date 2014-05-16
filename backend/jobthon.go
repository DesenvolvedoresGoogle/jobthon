package gdgjobthon

import (
	"encoding/json"
	"log"
	"net/http"

	"github.com/go-martini/martini"
	"github.com/martini-contrib/render"

	"appengine"
	"appengine/datastore"
)

func AppEngine(c martini.Context, r *http.Request) {
	c.Map(appengine.NewContext(r))
}

func CORS(w http.ResponseWriter) {
	w.Header().Set("Access-Control-Allow-Origin", "*")
}

func init() {
	m := martini.Classic()
	m.Use(render.Renderer())
	m.Use(AppEngine)
	m.Use(CORS)

	m.Get("/empresas", listaEmpresas)
	m.Get("/empresa/:email", getEmpresa)
	m.Post("/empresa", addEmpresa)

	m.Get("/vagas", listaVagas)
	m.Get("/vaga/:email", getVaga)

	m.Get("/curriculo/:email", getCurriculo)
	m.Post("/curriculo", addCurriculo)

	// curriculosFake := []Curriculo{
	// 	Curriculo{
	// 		Nome:        "Ricardo Cherobin",
	// 		Idade:       27,
	// 		Cidade:      "Florianópolis",
	// 		Estado:      "SC",
	// 		Habilidades: []string{"PHP", "Java", "MySQL", "IA"},
	// 	},
	// 	Curriculo{
	// 		Nome:        "Nassor Paulino da Silva",
	// 		Idade:       29,
	// 		Cidade:      "Florianópolis",
	// 		Estado:      "SC",
	// 		Habilidades: []string{"PHP", "Java", "MySQL", "Postgres", "Go", "Python", "Ruby", "Ruby on Rails", "SolR", "Elasticsearch"},
	// 	},
	// }

	http.Handle("/", m)
}

type Analise struct {
	Id              int64 `json:"id" datastore:"-"`
	EmpresaId       datastore.Key
	VagaId          datastore.Key
	CurriculoId     datastore.Key
	Compatibilidade float64
}

// Empresa
// Empresa Model
type Empresa struct {
	Id       int64  `json:"id" datastore:"-"`
	Email    string `json:"email"`
	Telefone string `json:"email"`
	Nome     string `json:"nome"`
	Cidade   string `json:"cidade"`
	Estado   string `json:"estado"`
	Sobre    string `json:"sobre"`
}

// Empresa Controllers
func listaEmpresas(r render.Render) {
	empresasFake := []Empresa{
		Empresa{
			Nome:   "Dev Coop",
			Email:  "dev@coop.com.br",
			Cidade: "São Paulo",
			Estado: "SP",
			Sobre:  "Mauris iaculis est a vestibulum venenatis. Vestibulum justo lacus, aliquet sit amet est at, laoreet tincidunt sapien. Nunc ac lectus ultrices, iaculis mauris non, commodo tellus. Nullam dictum eleifend molestie. Integer malesuada turpis non sem hendrerit aliquet. Proin id convallis turpis. Mauris ornare id nibh vel rhoncus. Aliquam tincidunt nunc in vehicula mollis. Suspendisse posuere eros id augue congue viverra.",
		},
		Empresa{
			Nome:   "Hackthon Dev Team",
			Email:  "Hackthondev@team.com.br",
			Cidade: "Florianópolis",
			Estado: "SC",
			Sobre:  "É nois no POG: Do mesmo modo, o consenso sobre a necessidade de qualificação desafia a capacidade de equalização das direções preferenciais no sentido do progresso.",
		},
	}

	r.JSON(http.StatusOK, empresasFake)
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
	}

	r.JSON(http.StatusOK, empresa)
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

func listaVagas(r render.Render) {
	vagasFake := []Vaga{
		Vaga{
			Titulo:      "Programador Go",
			Sobre:       "É claro que a revolução dos costumes apresenta tendências no sentido de aprovar a manutenção de alternativas às soluções ortodoxas.",
			Habilidades: []string{"Go", "GAE", "Google Datastore"},
			Area:        "Desenvolvimento",
			Cidade:      "Florianópolis",
			Estado:      "SC",
			Ativa:       true,
			Contratacao: []string{"Estágio", "PJ"},
		},
		Vaga{
			Titulo:      "Frontend Developer Angular.js",
			Sobre:       "Por outro lado, a consulta aos diversos militantes faz parte de um processo de gerenciamento das posturas dos órgãos dirigentes com relação às suas atribuições.",
			Habilidades: []string{"Javascript", "Angular.js", "HTML", "CSS", "Bootstrap"},
			Area:        "Design",
			Cidade:      "Criciúma",
			Estado:      "SC",
			Ativa:       true,
			Contratacao: []string{"Estágio", "PJ", "CLT"},
		},
		Vaga{
			Titulo:      "Desenvolvedor Android",
			Sobre:       "Caros amigos, o acompanhamento das preferências de consumo cumpre um papel essencial na formulação das diversas correntes de pensamento.",
			Habilidades: []string{"Java", "Android", "BDD"},
			Area:        "Mobile",
			Cidade:      "Curitiba",
			Estado:      "PR",
			Ativa:       true,
			Contratacao: []string{"Estágio", "PJ", "CLT"},
		},
		// Stripper
	}

	r.JSON(http.StatusOK, vagasFake)
}

func getVaga() {

}

// Curriculo
type Curriculo struct {
	Id          int64    `json:"id" datastore:"-"`
	Email       string   `json:"email"`
	Nome        string   `json:"nome"`
	Idade       int64    `json:"idade"`
	Habilidades []string `json:"habilidades"`
	Cidade      string   `json:"cidade"`
	Estado      string   `json:"estado"`
	Telefone    string   `json:"telefone"`
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
	}

	r.JSON(http.StatusOK, curriculo)
}
