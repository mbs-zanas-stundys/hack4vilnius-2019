import { API_ENDPOINTS, FORMAT } from '@app-shared/constants';
import { T } from '@app-shared/translations';
import { IContainer, IContainerDTO, IContainerHistory } from '@app-shared/types';
import { get } from '@app-shared/utils';
import moment from 'moment';
import 'moment/locale/lt';

const mapTo = {
  container: (dto: IContainerDTO): IContainer => {
    if (dto.history) {
      dto.history.sort((a, b) => b.date.localeCompare(a.date));
    }

    const lastHistoryItem: IContainerHistory | undefined = dto.history && dto.history[0];

    return {
      ...dto,
      lastUnload: lastHistoryItem
        ? moment(lastHistoryItem.date).format(FORMAT.dateTime)
        : T.unknown,

      lastUnloadDays: lastHistoryItem
        ? moment().diff(moment(lastHistoryItem.date), 'days')
        : T.unknown,

      lastUnloadWords: lastHistoryItem
        ? `${moment(lastHistoryItem.date).fromNow()}, ${moment(lastHistoryItem.date).format(
            FORMAT.dateWeekday
          )}`
        : T.unknown,

      missedPickUp: typeof dto.missedPickUp === 'boolean' ? String(dto.missedPickUp) : T.unknown
    };
  }
};

export default {
  containerDetails: containerNo =>
    get<IContainerDTO>(API_ENDPOINTS.container.getContainer, {
      containerNo
    }).then(mapTo.container),

  containerHistory: containerNo =>
    get(API_ENDPOINTS.container.getContainerPickupHistory, {
      containerNo
    }),

  containersLowRatio: async (): Promise<IContainer[]> =>
    get<IContainerDTO[]>(API_ENDPOINTS.container.lowRatioContainers).then(c =>
      c.map(mapTo.container)
    ),

  containersMissedPickups: async (dateFor: string = '2019-09-16'): Promise<IContainer[]> =>
    get<IContainerDTO[]>(API_ENDPOINTS.container.getMissedContainers, undefined, {
      date: moment(dateFor).format(FORMAT.dateShort)
    })
      .then(c => c.map(mapTo.container))
      .catch(() => [])
};
