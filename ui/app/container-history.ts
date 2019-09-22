import { api } from './api';
import moment = require('moment');

class ContainerHistoryElement extends HTMLElement {
  constructor(props) {
    // Always call super first in constructor
    super();

    // Element functionality written in here

    // ...

    const containerNo = this.getAttribute('container-no') || '';

    if (containerNo) {
      this.innerHTML = '<span class="loader"></span>';

      Promise.all([api.containerDetails(containerNo), api.containerSchedule(containerNo)]).then(([res, schedule]) => {
        this.innerHTML = `
          <table>
            <tr><td>Gatvė</td><td>${res.street}</td></tr>
            <tr><td>Namas</td><td>${res.houseNo}</td></tr>
            <tr><td>Vietovė</td><td>${res.location}</td></tr>
            <tr><td>Talpa</td><td>${res.capacity} m³</td></tr>
            <tr><td>Vežėjas</td><td>${res.company}</td></tr>
            <tr><td>Pask. išvėžimas</td><td title="${res.lastUnload}">${res.lastUnloadWords}</td></tr>
            <tr><td>Sekantys vėžimai</td><td>${
              schedule.length
                ? schedule.map(s => moment(s.expectedDate).format('YYYY-MM-DD[, ] dddd')).join('<br/>')
                : 'Nėra'
            }</td></tr>
          </table>
        `;
      });
    }
  }
}

customElements.define('container-history', ContainerHistoryElement);
