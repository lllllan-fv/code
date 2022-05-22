// document.getElementById("A").onclick = function () {
//     $("a").removeClass("active");
//     var obj = document.getElementById('A');
//     obj.className += ' active';
//     Type = "A";
//     console.log(Type);
//     get_book_list()
// }
// document.getElementById("B").onclick = function () {
//     $("a").removeClass("active");
//     var obj = document.getElementById('B');
//     obj.className += ' active';
//     Type = "B";
//     console.log(Type);
//     get_book_list()
// }
// document.getElementById("C").onclick = function () {
//     Type = "C";
//     console.log(Type);
//     get_book_list()
// } 

//分类检索
var Type = "A";

$(document).ready(function () {
    console.log('ready');

    show_category_list();

    get_book_list();
})

function show_category_list() {
    // 自己补充
    var category = ["A：马克思主义、列宁主义、毛泽东思想、邓小平理论", "B：哲学、宗教", "C：社会科学总论"];
    var data = "<h5>分类检索</h5>";
    for (var i = 0; i < 26; ++i) {
        var c = String.fromCharCode(i + 65);
        data += "<a id='" + c + "' href='#随你自己改名' class='list-group-item list-group-item-action " + (i == 0 ? "active" : "") + "' onclick='category_click(this.id)'>" + category[i] + "</a>";
    }
    $("#category_list").html(data);
}

function category_click(id) {
    console.log("click category click");
    Type = id;
    console.log(Type);
    get_book_list()
}


// 从数据库获取的所有图书列表
var book_list = [];

// 当前页面显示的列表
var list = [];

// 获取所有的图书信息
function get_book_list() {
    $.ajax({
        url: 'http://online_library.lllllan.cn/api/read/search_book_by_type.php',
        async: false, // 取消异步
        type: 'POST',
        dataType: 'json',
        data: {
            "type": Type,
        },
        success: function (data) {
            console.log(data);
            book_list = data.data;
        },
        error: function () {
            alert("sth wrong");
        }
    });

    list = []
    for (var idx in book_list) {
        list.push(book_list[idx]);
    }

    show_book_list(book_list);
}

// 显示(筛选的)图书
function show_book_list(list) {
    var data = "";

    for (var idx in list) {
        var item = list[idx];

        var bookdata = "<div class='col-2' onclick='show_book_detail(" + idx + ")'>\
                            <div class='card'>\
                            <img src='"+ item.img + " 'class='card-img-top' alt='...'>\
                                <div class='card-body'>\
                                    <h5 class='card-title'>"+ item.name + "</h5>\
                                    <p class='card-text'>"+ item.introduction + "</p>\
                                </div>\
                            </div>\
                        </div>"

        data += bookdata;
    }
    //   list.splice(idx, 1);
    $("#booklist").html(data);


}
//点击图书后进入详情界面

//点击对应图书进行跳转
function show_book_detail(idx) {
    var id = list[idx].id;
    window.sessionStorage.setItem("id", id)
    console.log(id)
    // window.location.href='../html/bookDetail.html';
}



// function search() {
    // console.log("click search");
    // console.log($("#name").val());
//
    // var key = $("#name").val();
    // list = [];
//
    // for (var idx in opinion_list) {
        // var item = opinion_list[idx];
        // if (item.user_name.includes(key) || item.op_content.includes(key)) {
            // list.push(item);
        // }
    // }
//
    // show_opinion_list(list);
// }