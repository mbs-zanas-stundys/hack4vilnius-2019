import { SimpleMarkerSymbol } from 'esri/symbols';

export const API_BASE_URL = 'http://localhost:8080';
export const API_ENDPOINTS = {
  container: {
    getContainer: '/containers/{containerNo}',
    getContainerPickupHistory: '/containers/{containerNo}/history',
    getMissedContainers: '/containers/pickup',
    lowRatioContainers: '/containers/low-ratio'
  }
};

export const DEBUG_MODE = false;
export const START_COORDINATES = [25.277049088433326, 54.686439174502574];
export const COLORS = {
  error: '#FF4262',
  grey: '#ccc',
  primary: '#43A047',
  secondary: '#009DFF',
  warning: '#FDC010'
};

export const FORMAT = {
  dateDisplay: 'YYYY-MM-DD[, ] dddd',
  dateShort: 'YYYY-MM-DD',
  dateTime: 'YYYY-MM-DD HH:mm',
  dateWeekday: 'dddd'
};

export const DOM = {
  containerDataTypeIdSelector: '#container-data-type',
  legendBlockId: 'legendBlock',
  mapId: 'mapBlock'
};

export const DEFAULT_SYMBOL = new SimpleMarkerSymbol({
  outline: {
    color: 'white',
    width: 0.5
  },
  path:
    'M24.2784 7.29299C19.9829 3.06762 13.0185 3.06762 8.723 7.29299C7.70314 8.28937 6.89274 9.47956 6.33945 10.7936C5.78617 12.1077 5.50114 13.5191 5.50114 14.9449C5.50114 16.3707 5.78617 17.7821 6.33945 19.0961C6.89274 20.4102 7.70314 21.6004 8.723 22.5967L16.5 30.2486L24.2784 22.5967C25.2982 21.6004 26.1086 20.4102 26.6619 19.0961C27.2152 17.7821 27.5002 16.3707 27.5002 14.9449C27.5002 13.5191 27.2152 12.1077 26.6619 10.7936C26.1086 9.47956 25.2982 8.28937 24.2784 7.29299ZM16.5 18.5611C15.5815 18.5611 14.7194 18.2036 14.069 17.5546C13.4252 16.9094 13.0636 16.0351 13.0636 15.1236C13.0636 14.2121 13.4252 13.3378 14.069 12.6926C14.718 12.0436 15.5815 11.6861 16.5 11.6861C17.4185 11.6861 18.282 12.0436 18.931 12.6926C19.5748 13.3378 19.9364 14.2121 19.9364 15.1236C19.9364 16.0351 19.5748 16.9094 18.931 17.5546C18.282 18.2036 17.4185 18.5611 16.5 18.5611Z',
  size: 24
});
