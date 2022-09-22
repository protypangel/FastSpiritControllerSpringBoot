class FastSpirit {
    static #Menu
    static #Login
    static #Controller
    static set init ({ CONTROLLER_AUTHORITY, CONTROLLER_EMPTY }) {
        this.#Menu = new class {
            constructor () {
                const ChildrenLeft = document.querySelectorAll("#left div div")
                this.display("API", ChildrenLeft)

                for(const menu of document.querySelectorAll("#left div div")) menu.addEventListener("click", () => this.display(menu.id, ChildrenLeft))
            }
            display (element, ChildrenLeft) {
                for(let child of ChildrenLeft) child.classList = ["content"]
                document.querySelector(`#left div #${element}`).className = "content active"

                for (let child of document.querySelectorAll("#right > div")) child.className = ""
                document.querySelector(`#right #${element}`).className = "active"
            }
        }
        this.#Controller = new class {
             createMapping (controllers) {
                 document.querySelector("#right #API").innerHTML = controllers.html

                 const firstMapping = document.querySelectorAll("#right #API .tree .controller .method")[4]
                 if (firstMapping != undefined) this.changeMapping(firstMapping, controllers.controllers, 23)

                 const title = document.querySelectorAll("#right #API .tree .controller .title")[0]
                 title.addEventListener("click", () => this.chevron(title))

                 for (let mapping of document.querySelectorAll("#right #API .tree .controller .method")) mapping.addEventListener("click", () => this.changeMapping(mapping, controllers.controllers, 15))

                 for (let menu of document.querySelectorAll("#right #API .doc .menus div:not(.empty)")) menu.addEventListener("click", () => this.menu = menu.id)
             }
             chevron (element) {
                 const children = element.parentElement.children
                 const table = children.item(children.length - 1)
                 if (table.style.display === '') {
                     table.style.display = 'none'
                     element.getElementsByTagName("use")[0].setAttribute("href", "/fast-spirit/svg/chevron-right.svg#svg")
                 }
                 else {
                     table.style.display = ''
                     element.getElementsByTagName("use")[0].setAttribute("href", "/fast-spirit/svg/chevron-down.svg#svg")
                 }
             }
             changeMapping (element, controllers, reducerSize) {
                const x = parseInt(element.attributes.x.value, 10)
                const y = parseInt(element.attributes.y.value, 10)
                const mapping = controllers[x].mappings[y]

                document.querySelectorAll("#header .empty")[0].innerHTML = mapping.title

                this.header = ({
                    mapping,
                    httpMethodSelect: document.querySelector("#right #API .doc .top .httpMethods"),
                    pathsSelect: document.querySelector("#right #API .doc .top .paths"),
                    doc: document.querySelector("#right #API .doc")
                })
                this.center = mapping
                this.menu = null
             }
             displayElement (element) {
                if (element.style.display === '') element.style.display = 'none'
                else element.style.display = ''
             }
             // Afficher un menu
             set menu (pointer) {
                 // Si l'élément est null alors afficher le premier onglet pouvant être activer
                 if (pointer == null) pointer = document.querySelectorAll("#right #API .doc .center > div:not(.inactive):not(.exception)")[0].getAttribute("link")
                 // Afficher les bonnes colonnes au centre
                 document.querySelectorAll("#right #API .doc .center > div:not(.inactive):not(.exception)").forEach(selection => selection.classList = [])
                 document.querySelector(`#right #API .doc .center div[link="${pointer}"]`).classList = ['active']
                 // Surligner le bon header
                 document.querySelectorAll("#right #API .doc .menus > .active").forEach(selection => selection.classList = [])
                 document.querySelector(`#right #API .doc .menus > #${pointer}`).classList = ["active"]
             }
             // Initialiser les headers
             set header ({ mapping, httpMethodSelect, pathsSelect, doc }) {
                // Vider les options pour les methods du doc et les ajouter
                httpMethodSelect.replaceChildren()
                httpMethodSelect.innerHTML = mapping.httpMethods
                    .map(method => `<option value="${method}" class="${method}">${method}</option>`)
                    .join("")
                // A chaque changement de selection d'une option dans les methods du doc
                // Changer la couleur du doc par la valeur sélectionner
                httpMethodSelect.addEventListener("change", () => this.colorHttpMethod = {doc, value: httpMethodSelect.value })
                this.colorHttpMethod = { doc, value: httpMethodSelect.value }
                // Vider les options pour les paths du doc et les ajoutes
                pathsSelect.replaceChildren()
                pathsSelect.innerHTML = mapping.paths
                    .map(path => `<option value="${path}">${path}</option>`)
                    .join("")
             }
             set colorHttpMethod ({doc , value}) {
                doc.className = 'doc ' + value
             }
             set center (mapping) {
                 this.description = {
                     description : mapping.description,
                     access: mapping.accessApi
                 }
                 this.parameters = {
                     parameters: mapping.parameters,
                     variables: mapping.pathVariables
                 }
             }
             set inactiveHeader (element) {
                element.style.display = "none"
                element.classList = ["inactive"]
             }
             set description ({ description, access }) {
                if (description === '' && access.length === 0) {
                    document.querySelector("#right #API .doc .menus #Description").style.display = "none"
                    this.inactiveHeader = document.querySelector("#right #API .doc .center #controllersDescription")
                } else if (description === '') {
                    document.querySelector("#right #API .doc .center #controllersDescription .description").style.display = "none"
                    document.querySelector("#right #API .doc .center #controllersDescription .access").innerHTML += access
                        .map(value => `<p class="controllersDescription_value">${value}</p>`)
                        .join("")
                } else if (access.length === 0) {
                    document.querySelector("#right #API .doc .center #controllersDescription .access").style.display = "none"
                    document.querySelector("#right #API .doc .center  #controllersDescription .description").innerHTML += `<p class="controllersDescription_value">${description}</p>`
                } else {
                    document.querySelector("#right #API .doc .center #controllersDescription .access").innerHTML += access
                        .map(value => `<p class="controllersDescription_value">${value}</p>`)
                        .join("")
                    document.querySelector("#right #API .doc .center  #controllersDescription .description").innerHTML += `<p class="controllersDescription_value">${description}</p>`
                }
             }
             set parameters ({parameters, variables}) {
                console.log(parameters, variables)
             }
        }
        this.#Login = new class {
              isConnected = false
              Connected
              Disconnected
              constructor () {
                  this.Connected = document.getElementById("connected")
                  this.Disconnected = document.getElementById("disconnected")
                  this.replaceAPIEmpty()
                  this.Connected.style.display = "none"

                  for(let button of document.querySelectorAll("#right #LOGIN .button")) button.addEventListener("click", () => this.change())
              }
              change () {
                  if (this.isConnected) {
                      this.Connected.style.display = "none"
                      this.Disconnected.style.display = "flex"
                      this.isConnected = !this.isConnected
                  } else {
                      const username = this.Disconnected.getElementsByTagName("input").namedItem("disconnected_username").value
                      const password = this.Disconnected.getElementsByTagName("input").namedItem("disconnected_password").value

                      if (this.replaceAPI(username, password)) {
                          document.getElementById("username").innerHTML = username
                          this.isConnected = !this.isConnected
                          this.Connected.style.display = "flex"
                          this.Disconnected.style.display = "none"
                          const content = document.querySelector("#right #LOGIN #disconnected .content")
                          content.className = 'content'
                      } else {
                          const content = document.querySelector("#right #LOGIN #disconnected .content")
                          content.className = 'content alert'
                      }
                  }
              }
              replaceAPIEmpty () {
                  let controllers = null
                  $.ajax({
                      url: CONTROLLER_AUTHORITY,
                      type: 'GET',
                        headers: {
                            username: 'ADMIN',
                            password: 'ADMIN'
                        },
                      async: false,
                      success: function(fragment) {
                          controllers = fragment
                      }
                  })
                  FastSpirit.#Controller.createMapping(controllers)
              }
              replaceAPI (username, password) {
                  let controllers = null
                  $.ajax({
                      url: CONTROLLER_AUTHORITY,
                      type: 'GET',
                      async: false,
                      headers: {
                          username: username,
                          password: password
                      },
                      success: function(fragment) {
                          controllers = fragment
                      }
                  })
                  if (controllers != null) {
                     FastSpirit.#Controller.createMapping(controllers)
                     return true
                  }
                  else return false
             }
         }
    }
}