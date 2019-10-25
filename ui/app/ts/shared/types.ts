interface IGeoJsonPoint {
  x: number;
  y: number;
  type: 'Point';
  coordinates: [number, number];
}

export interface IContainerPickupHistoryDTO {
  capacity: number;
  containerNo: string;
  date: Date;
  garbageTruckNo: string;
  id: string;
  position: IGeoJsonPoint;
  ratio: number;
  weight: number;
}

export interface IContainerForDateDTO {
  containerNo: string;
  id: string;
  missedPickup: boolean;
  position: IGeoJsonPoint;
}

export type IContainerForMap = IContainerForDateDTO | IContainerPickupHistoryDTO;

export interface IPickup {
  date: string;
  externalId: number;
  garbageTruckRegNo: string;
  weight: number;
}

export interface ISchedule {
  expectedDate: string;
  externalId: string;
}

export interface IContainerDTO {
  capacity: number;
  company: string;
  containerNo: string;
  history: IPickup[];
  houseNo: string;
  id: string;
  location: string;
  position: IGeoJsonPoint;
  schedules: ISchedule[];
  street: string;
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
