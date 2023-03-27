let navbar = document.querySelector('.header .navbar');

document.querySelector('#menu-btn').onclick = () =>{
    navbar.classList.add('active');
}

document.querySelector('#nav-close').onclick = () =>{
    navbar.classList.remove('active');
}

let searchForm = document.querySelector('.search-form');

document.querySelector('#search-btn').onclick = () =>{
    searchForm.classList.add('active');
}

document.querySelector('#close-search').onclick = () =>{
    searchForm.classList.remove('active');
}

window.onscroll = () =>{
    navbar.classList.remove('active');

    if(window.scrollY > 0){
        document.querySelector('.header').classList.add('active');
    }else{
        document.querySelector('.header').classList.remove('active');
    }
};

window.onload = () =>{
    if(window.scrollY > 0){
        document.querySelector('.header').classList.add('active');
    }else{
        document.querySelector('.header').classList.remove('active');
    }
};


var swiper = new Swiper(".home-slider", {
    loop:true, 
    grabCursor:true,
    navigation: {
      nextEl: ".swiper-button-next",
      prevEl: ".swiper-button-prev",
    },
});

var swiper = new Swiper(".product-slider", {
    loop:true, 
    grabCursor:true,
    spaceBetween: 20,
    navigation: {
      nextEl: ".swiper-button-next",
      prevEl: ".swiper-button-prev",
    },
    breakpoints: {
        0: {
          slidesPerView: 1,
        },
        640: {
          slidesPerView: 2,
        },
        768: {
          slidesPerView: 3,
        },
        1024: {
          slidesPerView: 4,
        },
    },
});

var swiper = new Swiper(".review-slider", {
    loop:true, 
    grabCursor:true,
    spaceBetween: 20,
    navigation: {
      nextEl: ".swiper-button-next",
      prevEl: ".swiper-button-prev",
    },
    breakpoints: {
        0: {
          slidesPerView: 1,
        },
        640: {
          slidesPerView: 2,
        },
        768: {
          slidesPerView: 3,
        },
    },
});

var swiper = new Swiper(".blogs-slider", {
    loop:true, 
    grabCursor:true,
    spaceBetween: 10,
    navigation: {
      nextEl: ".swiper-button-next",
      prevEl: ".swiper-button-prev",
    },
    breakpoints: {
        0: {
          slidesPerView: 1,
        },
        768: {
          slidesPerView: 2,
        },
        991: {
          slidesPerView: 3,
        },
    },
});

var swiper = new Swiper(".clients-slider", {
    loop:true, 
    grabCursor:true,
    spaceBetween: 20,
    breakpoints: {
        0: {
          slidesPerView: 1,
        },
        640: {
          slidesPerView: 2,
        },
        768: {
          slidesPerView: 3,
        },
        1024: {
          slidesPerView: 4,
        },
    },
});


// PlayVideo JS
// let videoList = document.querySelectorAll('.video-list-container .list');
let videoList = document.querySelectorAll('.video-list-container .list');

videoList.forEach(vid =>{
   vid.onclick = () =>{
      videoList.forEach(remove =>{remove.classList.remove('active')});
      vid.classList.add('active');
      let title = vid.querySelector('.list-title').innerHTML;
      document.querySelector('.main-video-container .main-vid-title').innerHTML = title;

      if (vid.id == 'class1') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/iJZy0Ee3sU0';
      } else if (vid.id == 'class2') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/tsPAgsWO93o';
      } else if (vid.id == 'class3') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/iP_xxOklh1c';
      } else if (vid.id == 'class4') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/ap8OdP8p0C0';
      } else if (vid.id == 'class5') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/QdQIFs89HoY';
      } else if (vid.id == 'class6') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/KYb_whsdF6E';
      } else if (vid.id == 'class7') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/t1TV1l1xkPw';
      } else if (vid.id == 'class8') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/X5GCsSH27nQ';
      } else if (vid.id == 'class9') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/HuDfAQUXA2A';
      } else if (vid.id == 'class10') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/T8SNYWv1dzs';
      } else if (vid.id == 'class11') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/tc6K_b-G1BU';
      } else if (vid.id == 'class12') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/wOhG-oLPiTY';
      } else if (vid.id == 'class13') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/z5_-5sD68cs';
      } else if (vid.id == 'class14') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/cE6pUroCoT8';
      } else if (vid.id == 'class15') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/Sx3ASMXhAks';
      } else if (vid.id == 'class16') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/RJ3Uwx8HLac';
      } else if (vid.id == 'class17') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/G6KmLpbxgIc';
      } else if (vid.id == 'class18') {
        document.getElementById('main-video').src = 'https://www.youtube.com/embed/Zrxp5ohkUl0';
      }
   };
});


// function changeCss(tagId) {
//   num = tagId.id;
//   let videoList = document.querySelectorAll('.video-list-container .list');
// }
