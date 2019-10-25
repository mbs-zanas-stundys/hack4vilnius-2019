import moment from 'moment';
import 'moment/locale/lt';
import { API_ENDPOINTS, FORMAT } from '../shared/constants';
import { T } from '../shared/translations';
import {
  IContainer,
  IContainerDTO,
  IContainerForDateDTO,
  IContainerPickupHistoryDTO,
  IPickup
} from '../shared/types';
import { get } from '../shared/utils';

const mapTo = {
  container: (dto: IContainerDTO): IContainer => {
    if (dto.history) {
      dto.history.sort((a, b) => b.date.localeCompare(a.date));
    }

    const lastHistoryItem: IPickup | undefined = dto.history && dto.history[0];

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
        : T.unknown
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

  containersLowRatio: async (): Promise<IContainerPickupHistoryDTO[]> =>
    get<IContainerPickupHistoryDTO[]>(API_ENDPOINTS.container.lowRatioContainers),

  containersMissedPickups: async (
    dateFor: string = '2019-09-16'
  ): Promise<IContainerForDateDTO[]> =>
    get<IContainerForDateDTO[]>(API_ENDPOINTS.container.getMissedContainers, undefined, {
      date: moment(dateFor).format(FORMAT.dateShort)
    })
};
