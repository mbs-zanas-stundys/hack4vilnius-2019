import PointGeometry from 'esri/geometry/Point';
import Graphic from 'esri/Graphic';

import { featureLayer, view } from '../components/map';
import { API_BASE_URL } from './constants';
import { IContainer } from './types';

export const get = async <T = any>(
  url: string,
  pathParams: Record<string, any> = {},
  queryParams: Record<string, any> = {}
): Promise<T> => {
  const httpParams = constructQueryParams(queryParams);
  const constructedPath = constructUrl(pathParams, url);
  const res = await fetch(API_BASE_URL + constructedPath + httpParams);

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

export const onMapInteract = (callback: () => void) => {
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

function constructUrl(pathParams: Record<string, any>, url: string): string {
  return Object.entries(pathParams).reduce(
    (acc, [key, value]) => acc.replace(`:${key}`, value),
    url
  );
}

function constructQueryParams(queryParams: Record<string, any>): string {
  const entries = Object.entries(queryParams);

  if (!entries.length) {
    return '';
  }

  return entries.map(([key, value]) => `${encodeURI(key)}=${encodeURI(value)}`).join('&');
}
