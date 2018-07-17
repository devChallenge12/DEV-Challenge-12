import scrollDeteched from './components/scroll-deteched';
import renderData from './components/renderData';
import datePolifill from './components/datePolifill';

let data;
const elem = document.querySelector("#category-inner");
fetch('/data.json')
  .then((res) => res.json())
  .then(function(result) {
    data = result;
    renderData(data, elem); 
  })
  .catch(console.warn);
scrollDeteched();

const openAddPopupLinks = document.querySelectorAll('.add-popup-link');
const addPopup = document.getElementById('add-popup');

Array.prototype.slice.call(openAddPopupLinks,0).map(item => item.addEventListener('click', (e) => {
    e.preventDefault();
    addPopup.classList.add('active');
  }));

const closeAddPopup = (e) => {
  e && e.preventDefault();
  addPopup.classList.remove('active');
}

const closeAddPopupElement = document.querySelector('#close-add-popup');
closeAddPopupElement.addEventListener('click', closeAddPopup);

const formAddItemToCatalog = document.querySelector('#formAddItemToCatalog');
formAddItemToCatalog.addEventListener('submit', (e)=> {
  e.preventDefault();
  const newObj = Array.prototype.slice.call(e.target,0).reduce((prev,current) => {
    console.log(current);
    const next = Object.assign({}, prev);
    next[current.name]= current.value;
    return next;
  },{});
  newObj.date = new Date().toISOString();
  newObj.percent = 0;
  data.push(newObj);
  renderData([newObj],elem);
  closeAddPopup();
})

const infoPopup = document.querySelector('#info-popup');
const panel = document.querySelector('#panel');
const closeInfoPopup = document.querySelector('#close-info-popup');

elem.addEventListener('click', (e) => {
  if (e.target.closest('input')) {
    panel.classList.add('active');
  }
  else if (e.target.closest('.category__item')) {
    infoPopup.classList.add('active');
  }
});
closeInfoPopup.addEventListener('click', (e) => {
  infoPopup.classList.remove('active');
})

const btnSettings = document.querySelector('.btn-settings');
const btnSettingsClose = document.querySelector('.btn-settings-close');
const leftSidebar = document.querySelector('.left-sidebar');
btnSettings.addEventListener('click', () => {
  leftSidebar.classList.add('active');
});
btnSettingsClose.addEventListener('click', () => {
  leftSidebar.classList.remove('active');
});