import './components/container-history';
import * as map from './components/map';

import { T } from '@app-shared/translations';
import { api } from 'api';
import { DEBUG_MODE } from './shared/constants';
import { DataType } from './shared/types';
import { mapContainersToMapFeatures, replaceMapFeatures } from './shared/utils';

const overlayElement = $('.overlay')!;
const actionsOverlay = $('.actions-overlay')!;
const btnFindLocation = $('#btn-find-location')!;
const btnEnterAddress = $('#btn-enter-address')!;
const overlayButtons = $('.overlay-buttons')!;
const overlayForm = $('.overlay form')!;
const btnShowMenu = $('#btn-show-menu')!;
const btnShowMap = $('#btn-show-map')!;
const btnShowButtons = $('#btn-show-buttons')!;
const legendBlock = $('#legendBlock')!;
const btnSubmitAddress = $('#btn-submit-address')!;
const dataTypeSelect = $('#container-data-type')!;

btnFindLocation.click(e => {
  e.preventDefault();
  btnFindLocation.addClass('loading');
  map.locateWidget
    .locate()
    .then((coordinates: Position) =>
      fetchFeaturesByCoords(coordinates.coords.latitude, coordinates.coords.longitude)
    )
    .then(() => hideOverlay())
    .catch(() => {
      btnFindLocation.removeClass('loading');
      alert(T.geolocationFailed);
    });
});

btnEnterAddress.click(e => {
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

function hideOverlay() {
  legendBlock.removeClass('hide');
  actionsOverlay.removeClass('hide');
  overlayElement.addClass('hide');
  setTimeout(() => {
    btnFindLocation.removeClass('loading');
  }, 300);
}

function showOverlay() {
  legendBlock.addClass('hide');
  actionsOverlay.addClass('hide');
  overlayElement.removeClass('hide');
}

function fetchFeaturesByCoords(latitude, longitude) {
  const value = dataTypeSelect.val() as DataType;

  const addLegendClasses = () => {
    legendBlock.removeClass(Object.values(DataType).map(dt => 'legend--' + dt));
    legendBlock.addClass('legend--' + value);
  };

  const startLoading = () => {
    legendBlock.addClass('legend--loading');
  };

  const stopLoading = () => {
    legendBlock.removeClass('legend--loading');
  };

  startLoading();

  const dataByType: Record<DataType, () => void> = {
    [DataType.unloadRatio]: () => {
      api.containers.containersLowRatio().then(containers => {
        const features = mapContainersToMapFeatures(containers);

        map.featureLayer.set('renderer', map.unloadRenderer);
        replaceMapFeatures(features);
        addLegendClasses();
        stopLoading();
      });
    },
    [DataType.missedPickups]: () => {
      api.containers.containersMissedPickups().then(containers => {
        const features = mapContainersToMapFeatures(containers);

        map.featureLayer.set('renderer', map.missedPickUpRenderer);
        replaceMapFeatures(features);
        addLegendClasses();
        stopLoading();
      });
    }
  };

  dataByType[value]();
}

function setupDevMode() {
  map.view.when(() => {
    btnShowMap.click();
  });

  map.view.on('click', clickEvent => {
    // tslint:disable-next-line: no-console
    console.debug('view', {
      clickEvent,
      latitude: clickEvent.mapPoint.latitude,
      longitude: clickEvent.mapPoint.longitude,
      scale: map.view.scale,
      view: map.view,
      zoom: map.view.zoom
    });
  });
}

if (DEBUG_MODE) {
  setupDevMode();
}
