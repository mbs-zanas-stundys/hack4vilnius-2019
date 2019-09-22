export interface ContainerHistory {
  date: '2019-09-18T18:23:15';
  garbageTruckRegNo: 'KBE 709';
  weight: 45;
}

export interface ContainerDTO {
  capacity: number;
  company: string;
  containerNo: string;
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
  lastUnloadDays: number;
}

export interface CSVPoint<T> {
  graphic: {
    attributes: T;
  };
}