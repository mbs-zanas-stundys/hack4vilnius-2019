import { FORMAT } from '@app-shared/constants';
import { T } from '@app-shared/translations';
import { IContainer } from '@app-shared/types';
import { api } from 'api';
import moment = require('moment');

class ContainerHistoryElement extends HTMLElement {
  constructor() {
    super();

    const containerNo = this.getAttribute('container-no') || '';

    if (containerNo) {
      this.innerHTML = '<span class="loader"></span>';

      Promise.all([
        api.containers.containerDetails(containerNo),
        api.containers.containerHistory(containerNo)
      ]).then(([res, schedule]) => {
        this.innerHTML = this.makeTable(res, schedule);
      });
    }
  }

  private makeTable(res: IContainer, schedule: any): string {
    return `
          <table>
            <tr><td>${T.street}</td><td>${res.street}</td></tr>
            <tr><td>${T.house}</td><td>${res.houseNo}</td></tr>
            <tr><td>${T.place}</td><td>${res.location}</td></tr>
            <tr><td>${T.capacity}</td><td>${res.capacity} m³</td></tr>
            <tr><td>${T.carrier}</td><td>${res.company}</td></tr>
            <tr><td>${T.lastUnloadShort}</td><td title="${res.lastUnload}">${
      res.lastUnloadWords
    }</td></tr>
                <tr><td>${T.nextUnloads}</td><td>${
      schedule.length
        ? schedule.map(s => moment(s.expectedDate).format(FORMAT.dateDisplay)).join('<br/>')
        : T.none
    }</td></tr>
          </table>
        `;
  }
}

customElements.define('container-history', ContainerHistoryElement);
