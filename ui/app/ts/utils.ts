import PointGeometry from 'esri/geometry/Point';
import Graphic from 'esri/Graphic';

import { featureLayer, view } from './map';
import { Container } from './types';

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
  Object.entries(pathParams).forEach(([key, value]) => (constructedPath = constructedPath.replace(`:${key}`, value)));

  if (httpParams) {
    httpParams = '?' + httpParams;
  }

  const res = await fetch(baseUrl + constructedPath + httpParams);

  return await res.json();
};

export const debounce = <T extends Function>(func: T, wait: number, immediate): T => {
  var timeout;
  return function(this: any) {
    var context = this;
    var args = arguments;
    var later = function() {
      timeout = null;
      if (!immediate) func.apply(context, args);
    };
    var callNow = immediate && !timeout;
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
    if (callNow) func.apply(context, args);
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

export const mapContainersToMapFeatures = (containers: Container[]): Graphic[] => {
  return containers.map(
    c =>
      new Graphic({
        geometry: new PointGeometry({
          latitude: c.position.y,
          longitude: c.position.x
        }),
        attributes: c
      })
  );
};
