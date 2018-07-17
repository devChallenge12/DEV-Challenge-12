export default (data, elem) => {
  const fragment = document.createDocumentFragment();
  data.forEach(item => {
    let color;
    if (item.percent > 70) {
      color = 'success';
    } else if (item.percent > 40) {
      color = 'warn';
    }
    else {
      color = 'danger';
    }
    const template = `
    <div class="category__cell">
      <div class="input-checkbox">
          <input class="input-checkbox__input" type="checkbox" ${item.checked && 'checked'}>
      </div>
    </div>
    <div class="category__cell">
      ${item.word}
    </div>
    <div class="category__cell">
      ${item.translate}
    </div>
    <div class="category__cell">
          ${item.category ? item.category : "<div class='disabled-category'>Не обрана</div>"}
      
    </div>
    <div class="category__cell">
      ${item.date}
    </div>
    <div class="category__cell">
      <div class="progress progress--${color}">${item.percent}%</div>
    </div>`;
    const div = document.createElement('div');
    div.classList = 'category__item';
    div.innerHTML = template;
    fragment.appendChild(div);
  });
  elem.appendChild(fragment);
}
