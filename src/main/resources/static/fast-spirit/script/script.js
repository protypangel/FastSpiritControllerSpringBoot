class FastSpirit {
    static #Menu = new class {
       ChildrenRight
       ChildrenLeft
       constructor () {
           this.ChildrenRight = document.getElementById("right").children
           this.ChildrenLeft = document.getElementById("left").children[0].children
           this.display(document.getElementById("API"))

            for(const menu of this.ChildrenLeft) menu.addEventListener("click", () => this.display(menu))
       }
       display (element) {
           for(let child of this.ChildrenRight) child.style.display = "none"
           this.ChildrenRight.namedItem(element.id).style.display = "flex"
           for(let child of this.ChildrenLeft) child.className = "content"
           element.className = "content active"
       }
    }
    static #Login = new class {
        isConnected = false
        Connected
        Disconnected
        constructor () {
            this.Connected = document.getElementById("connected")
            this.Disconnected = document.getElementById("disconnected")
            this.change()
            for(let button of $("#right #LOGIN .button")) button.addEventListener("click", () => this.change())
        }
        change () {
            this.isConnected = !this.isConnected
            if (this.isConnected) {
                this.Connected.style.display = "none"
                this.Disconnected.style.display = "flex"
            } else {
                this.Connected.style.display = "flex"
                this.Disconnected.style.display = "none"

                const username = this.Disconnected.getElementsByTagName("input").namedItem("disconnected_username").value
                const password = this.Disconnected.getElementsByTagName("input").namedItem("disconnected_password").value

                document.getElementById("username").innerHTML = username

                this.replaceAPI(username, password)
            }
        }
        replaceAPI (username, password) {
            $.ajax({
                url: FastSpirit_CONTROLLER_URL,
                type: 'GET',
                headers: {
                    username: username,
                    password: password
                },
                success: function(fragment) {
                    $("#right #API").replaceWith(fragment)
                    $("#right #API").css("display", "none")
                }
            })
        }
    }
    static #Controller = new class {
        constructor () {
            const content = $("#right #API .content")[1]
            const httpMethod = $("#right #API .content .httpMethods")[0]
            content.className = 'content ' + httpMethod.value
            httpMethod.addEventListener("change", (event) => content.className = 'content ' + httpMethod.value)

            const title = $("#right #API .tree .controller .title")[0]
            title.addEventListener("click", () => this.chevron(title))
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
    }
}