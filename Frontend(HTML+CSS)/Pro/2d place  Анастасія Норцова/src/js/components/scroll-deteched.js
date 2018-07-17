const scrollDetect = () => {
  let lastScroll = 0;
  const header = document.querySelector('#header');

  window.onscroll = () => {
      let currentScroll = document.documentElement.scrollTop || document.body.scrollTop; // Get Current Scroll Value

      if (currentScroll > 0 && lastScroll <= currentScroll){
        lastScroll = currentScroll;
        header.classList.add('active');
      }else{
        lastScroll = currentScroll;
        header.classList.remove('active');
      }
  };
}


export default scrollDetect;