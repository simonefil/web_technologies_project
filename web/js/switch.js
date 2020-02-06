function switchView() {
    var map2d = document.getElementById('map');
    var map3d = document.getElementById('3d');
    if (map2d.style["display"] == 'none') {
        map2d.style.display = 'block';
        map3d.style.display = 'none';
    }
    else if (map3d.style["display"] == 'none') {
        map2d.style.display = 'none';
        map3d.style.display = 'block';
    }
}