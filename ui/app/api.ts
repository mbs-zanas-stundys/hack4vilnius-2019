import { get } from './utils';
import { ContainerDTO, Container, ContainerHistory, Schedule } from './types';
import moment from 'moment';
import 'moment/locale/lt';

export const api = {
  containersByCoordinates: async (lat, lon, distance): Promise<Container[]> =>
    get<ContainerDTO[]>('/containers', undefined, { lat, lon, distance }).then(c => c.map(mapContainerDto)),
  containersByAddress: (street, houseNo, flatNo) =>
    get('/containers', undefined, { street, houseNo, flatNo }).then(c => c.map(mapContainerDto)),
  containerDetails: containerNo => get<Container>('/containers/:containerNo', { containerNo }).then(mapContainerDto),
  containerHistory: containerNo => get('/containers/:containerNo/history', { containerNo }),
  containerSchedule: containerNo => get<Schedule[]>('/containers/:containerNo/schedules ', { containerNo })
};

const mapContainerDto = (dto: ContainerDTO): Container => {
  dto.history.sort((a, b) => b.date.localeCompare(a.date));
  const lastHistoryItem: ContainerHistory | undefined = dto.history[dto.history.length - 1];

  return {
    ...dto,
    lastUnload: lastHistoryItem ? moment(lastHistoryItem.date).format('YYYY-MM-DD HH:mm') : 'Nežinoma',
    lastUnloadWords: lastHistoryItem
      ? `${moment(lastHistoryItem.date).fromNow()}, ${moment(lastHistoryItem.date).format('dddd')}`
      : 'Nežinoma',
    lastUnloadDays: lastHistoryItem ? moment().diff(moment(lastHistoryItem.date), 'days') : 'Nežinoma'
  };
};
