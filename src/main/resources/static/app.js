const msnry = () => Masonry.data('.msnry');

const relayout = () => {
    msnry().layout();
};

let emptyItem = {
    text: '',
    id: 0,
    completed: false
};

var data = {
    lists: [],
    selectedList: undefined,
    newListModel: undefined,
    editListModel: undefined
};

// const API_URL = 'http://localhost:8080';
const API_URL = '';

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
                relayout();
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
                });
        },
        updateListItem: function (listId, item) {
            apiService.post(`list/${listId}/listItem/${item.id}`, JSON.stringify({
                text: item.text,
                completed: item.completed
            }));
        },
        deleteItem: function(list, item) {
            apiService.delete(`list/${list.id}/listItem/${item.id}`)
            .then(() => {
                list.items = list.items.filter(i => i.id !== item.id);
                relayout();
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
                Vue.nextTick(function() {
                    msnry().reloadItems();
                    relayout();
                });
            });

        },
        editSelectedList: function() {
            apiService.put(`list/${this.selectedList.id}`, JSON.stringify({
                title: this.editListModel
            })).then(() => {
                this.selectedList.title = this.editListModel;
                this.editListModel = undefined;
                this.selectedList = undefined;
            }).catch((err) => console.log("Error during put", err));
        },
        deleteList: function(list) {
            let shouldDelete = confirm("Are you sure you want to delete this list?");
            if (shouldDelete) {
                apiService.delete(`list/${list.id}`)
                .then(() => {
                    this.lists = this.lists.filter(l => l.id != list.id);
                    relayout();
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

