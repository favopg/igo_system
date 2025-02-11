const sidebar = document.getElementById('sidebar');
//const overlay = document.getElementById('overlay');
const sidebarToggle = document.getElementById('sidebarToggle');

function toggleSidebar() {
    sidebar.classList.toggle('active');
    //overlay.classList.toggle('active');
}

sidebarToggle.addEventListener('click', toggleSidebar);

//overlay.addEventListener('click', toggleSidebar);

// ESCキーでもサイドバーを閉じられるようにする
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape' && sidebar.classList.contains('active')) {
        toggleSidebar();
    }
});
