import { v4 } from "uuid";
import { DEMO_DATA } from "./demo";
const STORE_NAME = "STORE_NAME";

if (!localStorage.getItem(STORE_NAME)) {
  console.log("First run, setting some demo data");
  localStorage.setItem(STORE_NAME, JSON.stringify(DEMO_DATA));
}

export function getAll() {
  const items = localStorage.getItem(STORE_NAME);
  return (items && JSON.parse(items)) || [];
}

export function addItem(item) {
  item.id = v4();
  const newItems = [item].concat(getAll());
  localStorage.setItem(STORE_NAME, JSON.stringify(newItems));

  return newItems;
}

export function removeItem(id) {
  const newItems = getAll().filter(item => item.id !== id);
  localStorage.setItem(STORE_NAME, JSON.stringify(newItems));

  return newItems;
}
