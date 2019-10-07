import FeatureLayer from 'esri/layers/FeatureLayer';
import EsriMap from 'esri/Map';
import UniqueValueRenderer from 'esri/renderers/UniqueValueRenderer';
import ColorVariable from 'esri/renderers/visualVariables/ColorVariable';
import MapView from 'esri/views/MapView';
import LegendWidget from 'esri/widgets/Legend';
import LocateWidget from 'esri/widgets/Locate';
import moment from 'moment';

import { defaultSymbol, LEGEND_COLORS, START_COORDINATES } from './constants';
import { Container, CSVPoint, DataType } from './types';
import { SimpleRenderer } from 'esri/renderers';

export const companyRenderer = new UniqueValueRenderer({
  field: 'company' as keyof Container,
  legendOptions: {
    title: 'Paskutinis išvežimas'
  },
  defaultLabel: 'Nežinoma',
  // defaultSymbol: defaultSymbol.clone(),

  uniqueValueInfos: [
    {
      value: 'VSA Vilnius',
      label: 'VSA Vilnius',
      symbol: defaultSymbol.clone().set('color', LEGEND_COLORS[0])
    },
    {
      value: 'Ekonovus',
      label: 'Ekonovus',
      symbol: defaultSymbol.clone().set('color', LEGEND_COLORS[1])
    },
    {
      value: 'Ecoservice',
      label: 'Ecoservice',
      symbol: defaultSymbol.clone().set('color', LEGEND_COLORS[3])
    }
  ]
});

export const unloadRenderer = new UniqueValueRenderer({
  field: 'ratio' as keyof Container,
  legendOptions: {
    title: 'Paskutinis išvežimas'
  },
  defaultSymbol: defaultSymbol.clone(),
  visualVariables: [
    new ColorVariable({
      field: 'ratio' as keyof Container,
      legendOptions: {
        title: 'Paskutinis išvežimas'
      },
      stops: [
        {
          value: 0,
          color: LEGEND_COLORS[3],
          label: '<2.5 kg/m³'
        },
        {
          value: 2.5,
          color: LEGEND_COLORS[1],
          label: '2.5 — 5.0 kg/m³'
        },
        {
          value: 5,
          label: '5 — 7.5 kg/m³',
          color: LEGEND_COLORS[0]
        },
        {
          value: 7.5,
          label: '7.5 - 10.0 kg/m³',
          color: LEGEND_COLORS[2]
        },
        {
          value: 10,
          label: '>10.0 kg/m³',
          color: LEGEND_COLORS[2]
        }
      ]
    })
  ]
});

export const missedPickUpRenderer = new UniqueValueRenderer({
  field: 'missedPickUp' as keyof Container,
  legendOptions: {
    title: 'Praleisti vėžimai'
  },
  uniqueValueInfos: [
    {
      value: 'false',
      label: 'Nepraleista',
      symbol: defaultSymbol.clone().set('color', LEGEND_COLORS[0])
    },
    {
      value: 'true',
      label: 'Praleista',
      symbol: defaultSymbol.clone().set('color', LEGEND_COLORS[3])
    },
    {
      value: 'Nežinoma',
      label: 'Nežinoma',
      symbol: defaultSymbol.clone().set('color', '#ccc')
    }
  ]
  // defaultSymbol: defaultSymbol.clone()
});

const map = new EsriMap({
  basemap: 'streets-navigation-vector'
});

const view = new MapView({
  map: map,
  container: 'mapBlock',
  center: START_COORDINATES,
  ui: {
    components: []
  },
  zoom: 16,
  constraints: {
    minZoom: 14,
    snapToZoom: false
  }
});

const locateWidget = new LocateWidget({
  view

  // goToOverride: (view, goToParams) => {
  //   view.when(() => {
  //     return view.goTo(goToParams.target, goToParams.options).then(() => {
  //       view.emit('locate', goToParams);
  //     });
  //   });
  // }
});

const legendWidget = new LegendWidget({
  view,
  container: 'legendContentBlock'
});

const featureLayer = new FeatureLayer({
  renderer: new SimpleRenderer(),
  geometryType: 'point',
  visible: true,
  objectIdField: 'id' as keyof Container,
  source: [],
  fields: [
    {
      name: 'id' as keyof Container,
      type: 'oid'
    },
    {
      name: 'containerNo' as keyof Container,
      type: 'string'
    },
    {
      name: 'missedPickUp' as keyof Container,
      type: 'string'
    },
    {
      name: 'capacity' as keyof Container,
      type: 'double'
    },
    {
      name: 'weight' as keyof Container,
      type: 'double'
    },
    {
      name: 'ratio' as keyof Container,
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
      type: 'blob'
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

      const dataType = $('#container-data-type').val() as DataType;

      const dataByType: Record<DataType, Function> = {
        [DataType.lastUnload]: () => {
          return `<container-history container-no="${a.containerNo}"></container-history>`;
        },
        [DataType.missedPickups]: () => {
          return `<container-history container-no="${a.containerNo}"></container-history>`;
        },
        [DataType.unloadRatio]: () => {
          return `
            <table>
              <tr><td>Pask. vėžimo data</td><td>${moment(a.date).format('YYYY-MM-DD')}</td></tr>
              <tr><td>Pask. vėžimo svoris</td><td>${a.weight} kg</td></tr>
              <tr><td>Konteinerio talpa</td><td>${a.capacity} m³</td></tr>
              <tr><td>Iškr. santykis</td><td>${Math.round(a.ratio * 100) / 100} kg/m³</td></tr>
            </table>
          `;
        }
      };

      return dataByType[dataType]();
    },
    outFields: ['*']
  }
});

map.layers.add(featureLayer, 1);

view.ui.add(locateWidget, 'top-left');
view.ui.add('legendBlock', 'bottom-left');

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

export { view, map, featureLayer, locateWidget };
