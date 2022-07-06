class FastSpirit {
    static #Menu = new class {
       ChildrenRight
       ChildrenLeft
       constructor () {
           this.ChildrenRight = document.getElementById("right").children
           this.ChildrenLeft = document.getElementById("left").children[0].children
           this.display(document.getElementById("LOGIN"))

       }
       display (element) {
           for(let child of this.ChildrenRight) child.style.display = "none"
           this.ChildrenRight.namedItem(element.id).style.display = "flex"
           for(let child of this.ChildrenLeft) child.className = "content"
           element.className = "content active"
       }
    }
    static #Login = new class {
        isConnected = true
        Connected
        Disconnected
        constructor () {
            this.Connected = document.getElementById("connected")
            this.Disconnected = document.getElementById("disconnected")
            this.change()
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
                FastSpirit.replaceAPI(username, password)
            }
        }
    }
    static Menu (element) {
        this.#Menu.display(element)
    }
    static Login () {
        this.#Login.change()
    }
    static replaceAPI (username, password) {
        console.log(username, password)
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
/*
        $("#right #LOGIN #disconnected").css("display", "none")
        function replaceMenu (menu) {
            console.log("here")
            $.ajax({
                url: '[[${menus}]]',
                type: 'GET',
                headers: {},
                success: function(fragment) {
                    console.log("here")
                    menu.replaceWith(fragment)
                }
            })
        }

*/