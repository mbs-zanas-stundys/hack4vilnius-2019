export interface IContainerHistory {
  date: '2019-09-18T18:23:15';
  garbageTruckRegNo: 'KBE 709';
  weight: 45;
}

export interface IContainerDTO {
  capacity: number;
  company: string;
  containerNo: string;
  houseNo: string;
  date: Date;
  ratio: number;
  weight: number;
  garbageTruckNo: string;
  history: IContainerHistory[];
  id: string;
  location: string;
  position: { x: number; y: number; type: 'Point'; coordinates: [number, number] };
  street: string;
  missedPickUp?: boolean;
}

export interface IContainer extends Omit<IContainerDTO, 'missedPickUp'> {
  lastUnload: string;
  lastUnloadWords: string;
  lastUnloadDays: number | string;
  missedPickUp?: string;
}

export interface ICSVPoint<T> {
  graphic: {
    attributes: T;
  };
}

export enum DataType {
  unloadRatio = 'unload-ratio',
  missedPickups = 'missed-pickups'
}

export enum Unit {
  Kilograms = 'kg',
  CubicMeters = 'm³',
  KgsPerCubicMeter = 'kg/m³'
}
