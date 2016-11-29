(function () {
    "use strict";
    var handlers = {
        register: function (quantity) {
            return (function () {
                var defer = $.Deferred();
                $.ajax({
                    type: "POST",
                    url: CONTEXT_PATH + "polling/tasks",
                    content: "application/json",
                    data: "quantity=" + quantity,
                    dataType: "json",
                }).done(function (data) {
                    defer.resolve(data);
                }).fail(function (jqXHR, textStatus) {
                    defer.reject("登録に失敗しました");
                });
                return defer.promise();
            });
        },
        update: function (id) {
            return (function () {
                var defer = $.Deferred();
                $.ajax({
                    type: "GET",
                    url: CONTEXT_PATH + "polling/tasks/" + id,
                    content: "application/json",
                    dataType: "json",
                }).done(function (data) {
                    defer.resolve(data);
                }).fail(function (jqXHR, textStatus) {
                    defer.reject("更新に失敗しました");
                });
                return defer.promise();
            });
        },

        wait: function (time) {
            return (function (data) {
                var d = $.Deferred()
                setTimeout(function () {
                    d.resolve(data);
                }, time);
                return d.promise()
            });
        },

        polling: function () {
            return (function (data) {
                var d = jQuery.Deferred();
                $.when()
                    .then(function () {
                        var d = $.Deferred();
                        if (data.done !== data.total) {
                            if ($("#" + data.id).size()) {
                                $("#" + data.id).remove();
                                $("ul").append('<li id="' + data.id + '">' + data.done + '/' + data.total + '</li>')
                            } else {
                                $("ul").append('<li id="' + data.id + '">' + data.done + '/' + data.total + '</li>')
                            }
                            d.resolve();
                        } else {
                            if ($("#" + data.id).size()) {
                                $("#" + data.id).remove();
                                $("ul").append('<li id="' + data.id + '">' + data.done + '/' + data.total + '</li>')
                            } else {
                                $("ul").append('<li id="' + data.id + '">' + data.done + '/' + data.total + '</li>')
                            }
                        }
                        return d.promise();
                    })
                    .then(handlers.update(data.id))
                    .then(handlers.wait(1000))
                    .done(handlers.polling())//再帰ループ
                d.resolve();
                return d.promise();
            });
        }
    };

    $(function () {
        $("#btn").on("click", function () {
            //FIXME: Deferredしようとしたけど途中で中途半端に諦めた
            $.when()
                .then(handlers.register($("#quantity").val()))
                .then(handlers.polling())
                .fail(function(msg) {
                    window.alert(msg);
                });
        });

    });
}());
