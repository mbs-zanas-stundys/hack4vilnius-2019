import FeatureLayer from 'esri/layers/FeatureLayer';
import EsriMap from 'esri/Map';
import UniqueValueRenderer from 'esri/renderers/UniqueValueRenderer';
import ColorVariable from 'esri/renderers/visualVariables/ColorVariable';
import MapView from 'esri/views/MapView';
import LegendWidget from 'esri/widgets/Legend';
import LocateWidget from 'esri/widgets/Locate';

import { defaultSymbol, LEGEND_COLORS, VGTU_COORDINATES } from './constants';
import { Container, CSVPoint } from './types';

const map = new EsriMap({
  basemap: 'topo-vector'
});

const symbols = defaultSymbol.clone();
symbols.set('color', 'green');

const renderer = new UniqueValueRenderer({
  field: 'lastUnloadDays' as keyof Container,
  legendOptions: {
    title: 'Paskutinis išvežimas'
  },
  defaultLabel: 'Nežinoma',
  uniqueValueInfos: [
    {
      value: 3,
      label: 'Prieš 3 dienas',
      symbol: defaultSymbol.clone().set('color', LEGEND_COLORS[0])
    },
    {
      value: 4,
      label: 'Prieš 4 dienas',
      symbol: defaultSymbol.clone().set('color', LEGEND_COLORS[1])
    }
  ],
  defaultSymbol: defaultSymbol.clone().set('color', '#ccc')
  // visualVariables: [
  //   new ColorVariable({
  //     field: 'lastUnloadDays' as keyof Container,
  //     legendOptions: {
  //       title: 'Paskutinis išvežimas'
  //     },

  //     stops: [
  //       {
  //         value: 0,
  //         color: LEGEND_COLORS[3],
  //         label: 'Šiandien'
  //       },
  //       {
  //         value: 1,
  //         color: LEGEND_COLORS[2],
  //         label: 'Vakar'
  //       },
  //       {
  //         value: 3.5,
  //         label: 'Prieš 2 dienas',
  //         color: LEGEND_COLORS[1]
  //       },
  //       {
  //         value: 4.5,
  //         label: 'Prieš 4 dienas',
  //         color: LEGEND_COLORS[0]
  //       }
  //     ]
  //   })
  // ]
});

const view = new MapView({
  map: map,
  container: 'viewDiv',
  center: VGTU_COORDINATES,
  zoom: 16,
  constraints: {
    minZoom: 14,
    snapToZoom: false
  }
});

const locateWidget = new LocateWidget({
  view
});

const legendWidget = new LegendWidget({
  view,
  container: 'legendContentDiv'
});

const featureLayer = new FeatureLayer({
  renderer,
  geometryType: 'point',
  visible: true,
  objectIdField: 'containerNo' as keyof Container,
  source: [],
  fields: [
    {
      name: 'containerNo' as keyof Container,
      type: 'oid'
    },
    {
      name: 'capacity' as keyof Container,
      type: 'double'
    },
    {
      name: 'company' as keyof Container,
      type: 'string'
    },
    {
      name: 'houseNo' as keyof Container,
      type: 'string'
    },
    {
      name: 'street' as keyof Container,
      type: 'string'
    },
    {
      name: 'location' as keyof Container,
      type: 'string'
    },
    {
      name: 'position' as keyof Container,
      type: 'geometry'
    },
    {
      name: 'history' as keyof Container,
      type: 'blob'
    },
    {
      name: 'lastUnload' as keyof Container,
      type: 'string'
    },
    {
      name: 'lastUnloadDays' as keyof Container,
      type: 'integer'
    },
    {
      name: 'lastUnloadWords' as keyof Container,
      type: 'string'
    }
  ],
  popupEnabled: true,
  spatialReference: {
    wkid: 4326
  },
  popupTemplate: {
    title: (point: CSVPoint<Container>) => {
      return (
        point.graphic.attributes.containerNo +
        ' — ' +
        point.graphic.attributes.street +
        ' ' +
        point.graphic.attributes.houseNo
      );
    },
    content: (point: CSVPoint<Container>) => {
      const a = point.graphic.attributes;
      console.log({ a });
      return `
        <table>
          <tr><td>Gatvė</td><td>${a.street}</td></tr>
          <tr><td>Namas</td><td>${a.houseNo}</td></tr>
          <tr><td>Vietovė</td><td>${a.location}</td></tr>
          <tr><td>Talpa</td><td>${a.capacity} m³</td></tr>
          <tr><td>Vežėjas</td><td>${a.company}</td></tr>
          <tr><td>Pask. išvėžimas</td><td title="${a.lastUnload}">${a.lastUnloadWords}</td></tr>
        </table>`;
    },
    outFields: ['*']
  }
});

map.layers.add(featureLayer, 1);

view.ui.add(locateWidget, 'top-left');
view.ui.add('legendDiv', 'bottom-left');

view.on('click', clickEvent => {
  console.log('view', {
    clickEvent,
    view,
    zoom: view.zoom,
    scale: view.scale,
    latitude: clickEvent.mapPoint.latitude,
    longitude: clickEvent.mapPoint.longitude
  });
});

locateWidget.on('click', () => console.log('locateWidget'));

export { view, map, featureLayer, locateWidget };
