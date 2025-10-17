function sendData(x, y, r) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: `main`,
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({x: x, y: y, r: r}),
            success: () => {
                addPoint({x: x, y: y});
                resolve();
            },
            error: (jqXHR, textStatus, errorThrown) => {
                console.error(`Error: ${jqXHR.responseText} ${textStatus}, ${errorThrown}`);
                alert(jqXHR.responseText);
                reject(errorThrown);
            }
        });
    });
}

function getPage(page, size = pageSize) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: `main?mode=cache&page=${page}&size=${size}`,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                const retrievedPage = {
                    page: page,
                    size: size,
                    hasBefore: data.hasBefore,
                    hasNext: data.hasNext,
                    dots: data.dots
                };
                resolve(retrievedPage);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(jqXHR.responseText);
                reject(errorThrown);
            }
        });
    });
}

function clearContext() {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: `main`,
            type: 'DELETE',
            success: resolve,
            error: function (jqXHR, textStatus, errorThrown) {
                console.error(`Error: ${jqXHR.responseText} ${textStatus}, ${errorThrown}`);
                alert(jqXHR.responseText);
                reject(errorThrown);
            }
        });
    });
}

function getCsv() {
    $.ajax({
        url: `main?mode=csv`,
        type: 'GET',
        dataType: 'text',
        success: function (data) {
            const blob = new Blob([data], {type: 'text/csv'});
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'data.csv';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.error(`Error: ${jqXHR.responseText} ${textStatus}, ${errorThrown}`);
            alert(jqXHR.responseText);
        }
    });
}

$(function () {
    $("#form").on("submit", function (e) {
        e.preventDefault();
        let x = parseFloat($('input[name="x"]').val());
        let y = parseFloat($('input[name="y"]').val());
        let r = parseFloat($('input[name="r"]').val());
        sendData(x, y, r).then(updateCurrent);
    })
})

$("#download-csv").on("click", function (e) {
    e.preventDefault();
    getCsv();
});
