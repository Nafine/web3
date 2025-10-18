function handleR(){
    let radius = parseFloat($('#r-input').val());
    refresh(radius);
}

$('#requestTable tbody').on('click mouseenter mouseleave', 'tr', function (event) {
    let row = $(this).children();

    let x = parseFloat(row.eq(0).text());
    let y = parseFloat(row.eq(1).text());

    if (event.type === 'mouseenter') {
        drawDot({x: x, y: y}, 'red');
    } else if (event.type === 'mouseleave') {
        refresh(parseFloat($('#r-input').val()));
    }
});

function getCursorPosition(canvas, event) {
    const rect = canvas.getBoundingClientRect();
    const x = (event.clientX - rect.left - 250) / 200;
    const y = (event.clientY - rect.top - 250) / -200;
    return {x: x, y: y};
}