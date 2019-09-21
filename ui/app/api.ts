import { get } from './utils';
import { ContainerDTO, Container, ContainerHistory } from './types';
import moment from 'moment';
import 'moment/locale/lt';

const mapContainerDto = (dto: ContainerDTO): Container => {
  dto.history.sort((a, b) => b.date.localeCompare(a.date));
  const lastHistoryItem: ContainerHistory | undefined = dto.history[dto.history.length - 1];

  return {
    ...dto,
    lastUnload: lastHistoryItem ? moment(lastHistoryItem.date).format('YYYY-MM-DD HH:mm') : '',
    lastUnloadWords: lastHistoryItem
      ? `${moment(lastHistoryItem.date).fromNow()}, ${moment(lastHistoryItem.date).format('dddd')}`
      : '',
    lastUnloadDays: lastHistoryItem ? moment().diff(moment(lastHistoryItem.date), 'days') : 0
  };
};

export const api = {
  containersByCoordinates: async (lat, lon, distance): Promise<Container[]> =>
    get<ContainerDTO[]>('/containers', undefined, { lat, lon, distance }).then(c => c.map(mapContainerDto)),
  containersByAddress: (street, houseNo, flatNo) => get('/containers', undefined, { street, houseNo, flatNo }),
  containerSchedule: containerId => get('/container/:containerId', { containerId })
};
