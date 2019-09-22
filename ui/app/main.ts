import PointGeometry from 'esri/geometry/Point';
import Graphic from 'esri/Graphic';

import { api } from './api';
import { SEARCH_RADIUS, PRODUCTION } from './constants';
import * as map from './map';
import { ContainerDTO, DataType } from './types';
import { debounce, onMapInteract, replaceMapFeatures, mapContainersToMapFeatures } from './utils';
import './container-history';

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
const btnSubmitAddress = $('#btn-submit-address')!;
const dataTypeSelect = $('#container-data-type')!;

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

btnSubmitAddress.click(e => {
  e.preventDefault();
  fetchFeaturesByCoords(map.view.center.latitude, map.view.center.longitude);
  setTimeout(() => {
    overlayButtons.removeClass('d-none');
    overlayForm.addClass('d-none');
  }, 300);
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

dataTypeSelect.change(() => {
  fetchFeaturesByCoords(map.view.center.latitude, map.view.center.longitude);
});

if (!PRODUCTION) {
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
  const value = dataTypeSelect.val() as DataType;

  const addLegendClasses = () => {
    legendDiv.removeClass(Object.values(DataType).map(t => 'legend--' + t));
    legendDiv.addClass('legend--' + value);
  };

  const dataByType: Record<DataType, Function> = {
    [DataType.lastUnload]: () => {
      api
        .containersByCoordinates(
          latitude,
          longitude,
          Math.max(map.view.extent.height, map.view.extent.width, SEARCH_RADIUS) / 2
        )
        .then(containers => {
          const features = mapContainersToMapFeatures(containers);

          map.featureLayer.set('renderer', map.companyRenderer);
          replaceMapFeatures(features);
          addLegendClasses();
        });
    },
    [DataType.unloadRatio]: () => {
      api.containersLowRatio().then(containers => {
        const features = mapContainersToMapFeatures(containers);
        console.log('unloadRatio', { containers });

        map.featureLayer.set('renderer', map.unloadRenderer);
        replaceMapFeatures(features);
        addLegendClasses();
      });
    }
  };

  dataByType[value]();
}

map.view.when(() => {
  onMapInteract(() => {
    debouncedFetchFeaturesByCoords(map.view.center.latitude, map.view.center.longitude);
  });
});
