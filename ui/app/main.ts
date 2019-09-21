import PointGeometry from 'esri/geometry/Point';
import Graphic from 'esri/Graphic';

import { api } from './api';
import { PROD, SEARCH_RADIUS } from './constants';
import * as map from './map';
import { ContainerDTO } from './types';
import { debounce, onMapInteract, replaceMapFeatures, mapContainersToMapFeatures } from './utils';

const debouncedFetchFeaturesByCoords = debounce(fetchFeaturesByCoords, 300, false);
const overlayElement = $('.overlay')!;
const actionsOverlay = $('.actions-overlay')!;
const btnFindLocation = $('#btn-find-location')!;
const btnEnterAdress = $('#btn-enter-address')!;
const overlayButtons = $('.overlay-buttons')!;
const overlayForm = $('.overlay form')!;
const btnShowMenu = $('#btn-show-menu')!;
const btnShowMap = $('#btn-show-map')!;
const btnShowButtons = $('#btn-show-buttons')!;
const legendDiv = $('#legendDiv')!;

btnFindLocation.click(e => {
  e.preventDefault();
  btnFindLocation.addClass('loading');
  map.locateWidget
    .locate()
    .then((coordinates: Position) => fetchFeaturesByCoords(coordinates.coords.latitude, coordinates.coords.longitude))
    .then(() => hideOverlay())
    .catch(() => {
      console.log('Could not find location');
      btnFindLocation.removeClass('loading');
      alert('Nepavyko nustatyti jūsų pozicijos. Bandykite dar kartą');
    });
});

btnEnterAdress.click(e => {
  e.preventDefault();
  overlayButtons.addClass('d-none');
  overlayForm.removeClass('d-none');
});

btnShowMap.click(e => {
  e.preventDefault();
  fetchFeaturesByCoords(map.view.center.latitude, map.view.center.longitude);
  hideOverlay();
});

btnShowMenu.click(e => {
  e.preventDefault();
  showOverlay();
});

btnShowButtons.click(e => {
  e.preventDefault();
  overlayButtons.removeClass('d-none');
  overlayForm.addClass('d-none');
});

if (!PROD) {
  map.view.when(() => {
    btnShowMap.click();
  });
}

function hideOverlay() {
  legendDiv.removeClass('hide');
  actionsOverlay.removeClass('hide');
  overlayElement.addClass('hide');
  setTimeout(() => {
    btnFindLocation.removeClass('loading');
  }, 300);
}
function showOverlay() {
  legendDiv.addClass('hide');
  actionsOverlay.addClass('hide');
  overlayElement.removeClass('hide');
}

function fetchFeaturesByCoords(latitude, longitude) {
  api
    .containersByCoordinates(
      latitude,
      longitude,
      Math.max(map.view.extent.height, map.view.extent.width, SEARCH_RADIUS) / 3.14
    )
    .then(containers => {
      console.log({ containers });
      const features = mapContainersToMapFeatures(containers);

      replaceMapFeatures(features);

      onMapInteract(() => {
        debouncedFetchFeaturesByCoords(map.view.center.latitude, map.view.center.longitude);
      });
    });
}
