import { get } from './utils';

export const api = {
  containersByCoordinates: (lat, lon, distance) => get('/containers', undefined, { lat, lon, distance }),
  containersByAddress: (street, houseNo, flatNo) => get('/containers', undefined, { street, houseNo, flatNo }),
  containerSchedule: containerId => get('/container/:containerId', { containerId })
};
