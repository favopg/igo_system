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


// ここからはメニュー部分
const naviSidever = Vue.createApp({
    data() {
        return {
        };
    },
});

naviSidever.component("navi_sideber", {
    props: ['activekbn'],
    template: `
		<div class="sidebar-content">
    		<h3 class="text-white mb-4">メニュー</h3>
              <ul class="nav flex-column mb-auto nav-pills">
            		<li class="nav-item mb-2">
                    	<a href="match_list.html" class="nav-link text-white" :class="activekbn === '1' ? 'active':'' ">
                        	<i class="bi bi-list-ul me-2"></i>対戦一覧
                    	</a>
                	</li>
                	<li class="nav-item mb-2">
                    	<a href="register.html" class="nav-link text-white" :class="activekbn === '2' ? 'active':'' ">
                        	<i class="bi bi-plus-circle me-2"></i>対戦登録
                    	</a>
                	</li>
                	<li class="nav-item mb-2">
                    	<a href="upload_csv.html" class="nav-link text-white" :class="activekbn === '3' ? 'active':'' ">
                        	<i class="bi bi-file-earmark-arrow-up me-2"></i>CSV取込
                    	</a>
                	</li>
            	</ul>
    	</div>
    	<button class="btn btn-danger w-100">
            <i class="bi bi-box-arrow-right me-2"></i>ログアウト
      </button>
	`,
});
naviSidever.mount('#sidebar')
