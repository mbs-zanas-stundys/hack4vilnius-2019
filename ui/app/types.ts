export interface ContainerHistory {
  date: '2019-09-18T18:23:15';
  garbageTruckRegNo: 'KBE 709';
  weight: 45;
}

export interface ContainerDTO {
  capacity: number;
  company: string;
  containerNo: string;
  date: Date;
  ratio: number;
  weight: number;
  garbageTruckNo: string;
  history: ContainerHistory[];
  houseNo: string;
  id: string;
  location: string;
  position: { x: number; y: number; type: 'Point'; coordinates: [number, number] };
  street: string;
}

export interface Container extends ContainerDTO {
  lastUnload: string;
  lastUnloadWords: string;
  lastUnloadDays: number | string;
}

export interface CSVPoint<T> {
  graphic: {
    attributes: T;
  };
}

export interface Schedule {
  id: string;
  containerNo: string;
  company: string;
  expectedDate: string;
  actualDate?: string;
  completed?: boolean;
}

export enum DataType {
  lastUnload = 'last-unload',
  unloadRatio = 'unload-ratio'
}
