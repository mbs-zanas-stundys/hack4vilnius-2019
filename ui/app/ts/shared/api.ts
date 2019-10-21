import moment from 'moment';
import 'moment/locale/lt';
import { IContainer, IContainerDTO, IContainerHistory, ISchedule } from './types';
import { get } from './utils';

export const api = {
  containerDetails: containerNo =>
    get<IContainerDTO>('/containers/:containerNo', {
      containerNo
    }).then(mapContainerDto),
  containerHistory: containerNo =>
    get('/containers/:containerNo/history', {
      containerNo
    }),
  containerSchedule: containerNo =>
    get<ISchedule[]>('/containers/:containerNo/schedules ', {
      containerNo
    }),
  containersByAddress: (street, houseNo, flatNo) =>
    get<IContainerDTO[]>('/containers', undefined, {
      flatNo,
      houseNo,
      street
    }).then(c => c.map(mapContainerDto)),

  containersByCoordinates: async (lat, lon, distance): Promise<IContainer[]> =>
    get<IContainerDTO[]>('/containers', undefined, {
      distance,
      lat,
      lon
    }).then(c => c.map(mapContainerDto)),

  containersLowRatio: async (): Promise<IContainer[]> =>
    get<IContainerDTO[]>('/containers/low-ratio').then(c => c.map(mapContainerDto)),
  containersMissedPickups: async (): Promise<IContainer[]> =>
    get<IContainerDTO[]>('/containers/delayed', undefined, {
      date: moment('2019-09-16').format('YYYY-MM-DD')
    })
      .then(c => c.map(mapContainerDto))
      .catch(() => [])
};

const mapContainerDto = (dto: IContainerDTO): IContainer => {
  if (dto.history) {
    dto.history.sort((a, b) => b.date.localeCompare(a.date));
  }
  const lastHistoryItem: IContainerHistory | undefined = dto.history && dto.history[0];

  return {
    ...dto,
    lastUnload: lastHistoryItem
      ? moment(lastHistoryItem.date).format('YYYY-MM-DD HH:mm')
      : 'Nežinoma',
    lastUnloadDays: lastHistoryItem
      ? moment().diff(moment(lastHistoryItem.date), 'days')
      : 'Nežinoma',
    lastUnloadWords: lastHistoryItem
      ? `${moment(lastHistoryItem.date).fromNow()}, ${moment(lastHistoryItem.date).format('dddd')}`
      : 'Nežinoma',
    missedPickUp: typeof dto.missedPickUp === 'boolean' ? String(dto.missedPickUp) : 'Nežinoma'
  };
};
