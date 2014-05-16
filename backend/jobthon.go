package jobthon

import (
	"net/http"

	"appengine"
	"appengine/datastore"

	"github.com/go-martini/martini"
	"github.com/martini-contrib/render"
)

func AppEngine(c martini.Context, r *http.Request) {
	c.Map(appengine.NewContext(r))
}

func init() {
	m := martini.Classic()
	m.Use(render.Renderer())
	m.Use(AppEngine)

	m.Get("/empresas", listaEmpresas)
	m.Get("/vagas", listaVagas)

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
	Id     int64  `json:"id" datastore:"-"`
	Nome   string `json:"nome"`
	Cidade string `json:"cidade"`
	Estado string `json:"estado"`
	Sobre  string `json:"sobre"`
}

func (e *Empresa) Salvar() error {
	return nil
}

func ListaEmpresas() []Empresa {
	return []Empresa{
		Empresa{
			Nome:   "Dev Coop",
			Cidade: "São Paulo",
			Estado: "SP",
			Sobre:  "Mauris iaculis est a vestibulum venenatis. Vestibulum justo lacus, aliquet sit amet est at, laoreet tincidunt sapien. Nunc ac lectus ultrices, iaculis mauris non, commodo tellus. Nullam dictum eleifend molestie. Integer malesuada turpis non sem hendrerit aliquet. Proin id convallis turpis. Mauris ornare id nibh vel rhoncus. Aliquam tincidunt nunc in vehicula mollis. Suspendisse posuere eros id augue congue viverra.",
		},
		Empresa{
			Nome:   "Hackthon Dev Team",
			Cidade: "Florianópolis",
			Estado: "SC",
			Sobre:  "É nois no POG: Do mesmo modo, o consenso sobre a necessidade de qualificação desafia a capacidade de equalização das direções preferenciais no sentido do progresso.",
		},
	}
}

// Empresa Controllers
func listaEmpresas(r render.Render) {
	r.JSON(200, ListaEmpresas())
}

func addEmpresa(r render.Render) {
}

func getEmpresa(key datastore.Key, r render.Render) {

}

type Vaga struct {
	Id          int64    `json:"id" datastore:"-"`
	Titulo      string   `json:"titulo"`
	Sobre       string   `json:"sobre"`
	Habilidades []string `json:"habilidades"`
	Area        string   `json:"area"`
	Cidade      string   `json:"cidade"`
	Estado      string   `json:"estado"`
	Ativa       bool     `json:"ativa"`
	Contratacao []string `json:"contratacao"`
}

func ListaVagas() []Vaga {
	return []Vaga{
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
}

func listaVagas(r render.Render) {
	// filtrar pela área
	r.JSON(200, ListaVagas())
}

// Curriculo
type Curriculo struct {
	Id          int64    `json:"id" datastore:"-"`
	Nome        string   `json:"nome"`
	Idade       int64    `json:"idade"`
	Habilidades []string `json:"habilidades"`
	Cidade      string   `json:"cidade"`
	Estado      string   `json:"estado"`
}

func ListaCurriculo() []Curriculo {
	return []Curriculo{
		Curriculo{
			Nome:        "Ricardo Cherobin",
			Idade:       27,
			Cidade:      "Florianópolis",
			Estado:      "SC",
			Habilidades: []string{"PHP", "Java", "MySQL", "IA"},
		},
		Curriculo{
			Nome:        "Nassor Paulino da Silva",
			Idade:       29,
			Cidade:      "Florianópolis",
			Estado:      "SC",
			Habilidades: []string{"PHP", "Java", "MySQL", "Postgres", "Go", "Python", "Ruby", "Ruby on Rails", "SolR", "Elasticsearch"},
		},
	}
}
