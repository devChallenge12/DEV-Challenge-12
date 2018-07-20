export function getTouchData(e) {
  if (!e.changedTouches) return null;
  const touch = e.changedTouches[0];
  const data = {
    time: e.timeStamp,
    x: touch.clientX,
    y: touch.clientY
  };
  return data;
}

export function getYVelocity(touchStart, touchEnd) {
  return (touchEnd.y - touchStart.y) / (touchEnd.time - touchStart.time);
}

export function getXVelocity(touchStart, touchEnd) {
  return (touchEnd.x - touchStart.x) / (touchEnd.time - touchStart.time);
}

export function getThrowDurationFromVelocity(xVelocity, yVelocity) {
  return 1000 * 5 * (Math.abs(xVelocity) + Math.abs(yVelocity) / 2);
}

export function updateCoordianates(puckStyler, xVelocity, yVelocity) {
  const distance = 10;

  return puckStyler.set({
    y: puckStyler.get("y") + distance * yVelocity,
    x: puckStyler.get("x") + distance * xVelocity
  });
}

export function getThrowPoints(puckDomElement, goal) {
  const rect = puckDomElement.getBoundingClientRect();
  const x = rect.left + rect.width/2;
  const y = rect.top + rect.height/2;

  console.log(x, y, goal);

  const distance = Math.sqrt(Math.pow(goal.x - x, 2) + Math.pow(goal.y - y, 2));

  return (distance <= goal.distance/2) ? 1 : 0;
}
