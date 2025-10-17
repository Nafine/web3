let currentPage = 1;
let pageSize = 10;

function createTableRow(point) {
    return `<tr><td>${point.x.toFixed(2)}</td><td>${point.y.toFixed(2)}</td><td>${point.r.toFixed(2)}</td><td>${point.hit}</td><td>${point.requestTime}</td><td>${point.execTime}ns</td></tr>`;
}

function putPage(data) {
    emptyTable();
    data.dots.forEach(dot => {
        let point = {
            x: dot.req.x,
            y: dot.req.y,
            r: dot.req.r,
            hit: dot.resp.hit,
            requestTime: dot.resp.requestTime,
            execTime: dot.resp.execTime
        }
        addPoint({x: point.x, y: point.y});
        appendRow(point);
    })
    updatePaginationControls(data.hasBefore, data.hasNext);
}

function emptyTable() {
    $('#requestTable tbody').empty();
}

function appendRow(point) {
    const tbody = $('#requestTable tbody');
    tbody.append(createTableRow(point));
}

function updateCurrent() {
    getPage(currentPage).then(data => putPage(data));
}

function updatePaginationControls(hasBefore, hasNext) {
    $('#page-info').text(currentPage);
    $('#page-prev').prop('disabled', !hasBefore);
    $('#page-next').prop('disabled', !hasNext);
}

$('#page-prev').on('click', async function () {
    $('#page-prev').prop('disabled', true);
    let page = await getPage(currentPage - 1).catch(() => {
        $(this).prop('disabled', false);
    });
    if (page.dots.length !== 0) {
        currentPage--;
        putPage(page);
    }
});

$('#page-next').on('click', async function () {
    $('#page-next').prop('disabled', true);
    let page = await getPage(currentPage + 1).catch(() => {
        $(this).prop('disabled', false);
    });
    if (page.dots.length !== 0) {
        currentPage++;
        putPage(page);
    }
});

$('#page-size-select').on('change', function () {
    pageSize = parseInt($(this).val());
    currentPage = 1;
    updateCurrent();
});

$('#clear-points').on('click', function () {
    clearContext().then(clear).then(() => currentPage = 1).then(updateCurrent);
});

$(updateCurrent);