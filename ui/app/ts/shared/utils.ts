import PointGeometry from 'esri/geometry/Point';
import Graphic from 'esri/Graphic';

import { featureLayer, view } from '../components/map';
import { IContainer } from './types';

export const get = async <T = any>(
  url: string,
  pathParams: Record<string, any> = {},
  params: Record<string, any> = {}
): Promise<T> => {
  const baseUrl = 'http://localhost:8080';
  let httpParams = Object.entries(params)
    .map(([key, value]) => `${key}=${value}`)
    .join('&');

  let constructedPath = url;
  Object.entries(pathParams).forEach(
    ([key, value]) => (constructedPath = constructedPath.replace(`:${key}`, value))
  );

  if (httpParams) {
    httpParams = '?' + httpParams;
  }

  const res = await fetch(baseUrl + constructedPath + httpParams);

  return await res.json();
};

export const debounce = <T extends (...args: any) => void>(func: T, wait: number, immediate): T => {
  let timeout;
  return function(this: any) {
    const context = this;
    const args = arguments as any;
    const later = () => {
      timeout = null;
      if (!immediate) {
        func.apply(context, args);
      }
    };
    const callNow = immediate && !timeout;
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
    if (callNow) {
      func.apply(context, args);
    }
  } as any;
};

export const onMapInteract = callback => {
  view.on('drag', () => {
    callback();
  });
  view.on('mouse-wheel', () => {
    callback();
  });
  view.on('resize ', () => {
    callback();
  });
  view.on('locate', () => {
    callback();
  });
};

export const replaceMapFeatures = (features: Graphic[]) => {
  featureLayer.queryFeatures().then(oldFeatures => {
    featureLayer.applyEdits({
      addFeatures: features,
      deleteFeatures: oldFeatures.features
    });
  });
};

export const mapContainersToMapFeatures = (containers: IContainer[]): Graphic[] => {
  return containers.map(
    c =>
      new Graphic({
        attributes: c,
        geometry: new PointGeometry({
          latitude: c.position.y,
          longitude: c.position.x
        })
      })
  );
};
