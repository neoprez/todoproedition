
let emptyItem = {
    text: '',
    id: 0,
    completed: false
};

var data = {
    lists: [
        {
            items: []
        }
    ],
    selectedList: 0,
    newListModel: undefined,
    editListModel: undefined
};

const API_URL = 'http://localhost:8080';

const apiService = {
    call: async function (path, method, body, extraHeaders) {
        extraHeaders = extraHeaders || {};

        return fetch(`${API_URL}/${path}`, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                ...extraHeaders
            },
            body: body
        });
    },
    get: async function (path, body, params, extraHeaders) {
        return this.call(path, 'GET', body, extraHeaders);
    },
    post: async function (path, body, extraHeaders) {
        return this.call(path, 'POST', body, extraHeaders);
    },
    put: async function (path, body, extraHeaders) {
        return this.call(path, 'PUT', body, extraHeaders);
    },
    delete: async function (path, body, extraHeaders) {
        return this.call(path, 'DELETE', body, extraHeaders);
    }
};


var vm = new Vue({
    el: '#app',
    data: data,
    methods: {
        updateItemText: function (event) {
            var item = event.item;
            item.text = event.newText.trim();
            this.$emit('item-updated', item);
        },
        addNewItem: function (list) {
            let newItem = Object.assign({}, emptyItem);

            apiService.post(`list/${list.id}/listItem`, 
            JSON.stringify({ 
                text: ''
            }))
            .then(resp => resp.json())
            .then(data => {
                newItem.id = data.id;
                list.items.push(newItem);
            });
        },
        updateItemCompleted: function (item) {
            item.completed = !item.completed;
            this.$emit('item-updated', item);
        },
        loadLists: function () {
            var that = this;

            apiService.get('list')
                .then(resp => resp.json())
                .then(data => {
                    that.lists = data;
                    that.selectedList = 0;
                });
        },
        updateListItem: function (listId, item) {
            apiService.post(`list/${listId}/listItem/${item.id}`, JSON.stringify({
                text: item.text,
                completed: item.completed
            }));
        },
        deleteItem: function(listId, item) {
            apiService.delete(`list/${listId}/listItem/${item.id}`)
            .then(() => {
                this.lists[this.selectedList].items = this.lists[this.selectedList].items.filter(i => i.id !== item.id);
            })
            .catch(e => console.error("error", e));
        },
        createNewList: function() {
            apiService.post(`list`, JSON.stringify({
                title: this.newListModel
            }))
            .then(resp => resp.json())
            .then(data => {
                this.lists.push({
                    title: this.newListModel,
                    id: data.id,
                    items: []
                });
                this.newListModel = undefined;
                // var myModal = new bootstrap.Modal(document.getElementById('createBtn'), options)
            });
        },
        editSelectedList: function() {
            apiService.put(`list/${this.lists[this.selectedList].id}`, JSON.stringify({
                title: this.editListModel
            })).then(() => {
                this.lists[this.selectedList].title = this.editListModel;
                this.editListModel = undefined;
            }).catch((err) => console.log("Error during put", err));
        },
        deleteList: function() {
            let shouldDelete = confirm("Are you sure you want to delete this list?");
            if (shouldDelete) {
                let listId = this.lists[this.selectedList].id;
                apiService.delete(`list/${listId}`)
                .then(() => {
                    this.lists = this.lists.filter(list => list.id != listId);
                    this.selectedList = 0;
                })
                .catch((err) => console.error("Failed to delete list", err));
            }
        }
    },
    mounted: function () {
        this.loadLists();
    }
});

vm.$on('item-updated', function (item) {
    vm.updateListItem(1, item);
});

