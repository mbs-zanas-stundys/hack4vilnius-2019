import moment = require('moment');
import { api } from '../api/index';
import { FORMAT } from '../shared/constants';
import { T } from '../shared/translations';
import { IContainer, ISchedule } from '../shared/types';

class ContainerHistoryElement extends HTMLElement {
  constructor() {
    super();

    const containerNo = this.getAttribute('container-no') || '';

    if (containerNo) {
      this.innerHTML = '<span class="loader"></span>';

      Promise.all([api.containers.containerDetails(containerNo)]).then(([res]) => {
        this.innerHTML = this.makeTable(res);
      });
    }
  }

  private makeTable(res: IContainer): string {
    return `
          <table>
            <tr><td>${T.street}</td><td>${res.street}</td></tr>
            <tr><td>${T.house}</td><td>${res.houseNo}</td></tr>
            <tr><td>${T.place}</td><td>${res.location}</td></tr>
            <tr><td>${T.capacity}</td><td>${res.capacity} m³</td></tr>
            <tr><td>${T.carrier}</td><td>${res.company}</td></tr>
            <tr>
              <td>${T.lastUnloadShort}</td>
              <td title="${res.lastUnload}">${res.lastUnloadWords}</td>
            </tr>
            <tr><td>${T.nextUnloads}</td><td>${this.makeNextUnloads(res.schedules)}</td></tr>
          </table>
        `;
  }

  private makeNextUnloads(schedules: ISchedule[]) {
    const nextUnloads = schedules
      .filter(s => moment(s.expectedDate).isAfter())
      .map(s => moment(s.expectedDate).format(FORMAT.dateDisplay));

    return nextUnloads.length ? nextUnloads.join('<br/>') : T.none;
  }
}

customElements.define('container-history', ContainerHistoryElement);
