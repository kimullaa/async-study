(function () {
    "use strict";
    var handlers = {
        register: function (quantity) {
            return (function () {
                var defer = $.Deferred();
                $.ajax({
                    type: "POST",
                    url: CONTEXT_PATH + "batch/tasks/",
                    content: "application/json",
                    dataType: "json",
                    data: "quantity="+quantity
                }).done(function (data) {
                    defer.resolve(data);
                }).fail(function (jqXHR, textStatus) {
                    defer.reject("登録に失敗しました");
                });
                return defer.promise();
            });
        },

        updateView: function () {
            return (function (data) {
                var d = jQuery.Deferred();
                $.when()
                    .then(function () {
                        var d = $.Deferred();
                        if (data.done !== data.total) {
                            d.resolve();
                        } else {
                            window.alert("処理が完了しました")
                            d.resolve();
                        }
                        return d.promise();
                    });
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
                .then(handlers.updateView())
                .fail(function(msg) {
                    window.alert(msg);
                });
        });

    });
}());
