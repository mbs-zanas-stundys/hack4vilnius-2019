interface ContainerHistory {
  date: '2019-09-18T18:23:15';
  garbageTruckRegNo: 'KBE 709';
  weight: 45;
}

export interface Container {
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

export interface CSVPoint<T> {
  graphic: {
    attributes: T;
  };
}
