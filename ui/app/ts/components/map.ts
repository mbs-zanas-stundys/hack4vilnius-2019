import FeatureLayer from 'esri/layers/FeatureLayer';
import EsriMap from 'esri/Map';
import UniqueValueRenderer from 'esri/renderers/UniqueValueRenderer';
import ColorVariable from 'esri/renderers/visualVariables/ColorVariable';
import MapView from 'esri/views/MapView';
import LocateWidget from 'esri/widgets/Locate';
import moment from 'moment';

import { SimpleRenderer } from 'esri/renderers';
import { defaultSymbol, LEGEND_COLORS, PRODUCTION, START_COORDINATES } from '../shared/constants';
import { DataType, IContainer, ICSVPoint } from '../shared/types';

export const companyRenderer = new UniqueValueRenderer({
  defaultLabel: 'Nežinoma',
  field: 'company' as keyof IContainer,
  legendOptions: {
    title: 'Paskutinis išvežimas'
  },
  uniqueValueInfos: [
    {
      label: 'VSA Vilnius',
      symbol: defaultSymbol.clone().set('color', LEGEND_COLORS[0]),
      value: 'VSA Vilnius'
    },
    {
      label: 'Ekonovus',
      symbol: defaultSymbol.clone().set('color', LEGEND_COLORS[1]),
      value: 'Ekonovus'
    },
    {
      label: 'Ecoservice',
      symbol: defaultSymbol.clone().set('color', LEGEND_COLORS[3]),
      value: 'Ecoservice'
    }
  ]
});

export const unloadRenderer = new UniqueValueRenderer({
  defaultSymbol: defaultSymbol.clone(),
  field: 'ratio' as keyof IContainer,
  legendOptions: {
    title: 'Paskutinis išvežimas'
  },
  visualVariables: [
    new ColorVariable({
      field: 'ratio' as keyof IContainer,
      legendOptions: {
        title: 'Paskutinis išvežimas'
      },
      stops: [
        {
          color: LEGEND_COLORS[3],
          label: '<2.5 kg/m³',
          value: 0
        },
        {
          color: LEGEND_COLORS[1],
          label: '2.5 — 5.0 kg/m³',
          value: 2.5
        },
        {
          color: LEGEND_COLORS[0],
          label: '5 — 7.5 kg/m³',
          value: 5
        },
        {
          color: LEGEND_COLORS[2],
          label: '7.5 - 10.0 kg/m³',
          value: 7.5
        },
        {
          color: LEGEND_COLORS[2],
          label: '>10.0 kg/m³',
          value: 10
        }
      ]
    })
  ]
});

export const missedPickUpRenderer = new UniqueValueRenderer({
  field: 'missedPickUp' as keyof IContainer,
  legendOptions: {
    title: 'Praleisti vėžimai'
  },
  uniqueValueInfos: [
    {
      label: 'Nepraleista',
      symbol: defaultSymbol.clone().set('color', LEGEND_COLORS[0]),
      value: 'false'
    },
    {
      label: 'Praleista',
      symbol: defaultSymbol.clone().set('color', LEGEND_COLORS[3]),
      value: 'true'
    },
    {
      label: 'Nežinoma',
      symbol: defaultSymbol.clone().set('color', '#ccc'),
      value: 'Nežinoma'
    }
  ]
  // defaultSymbol: defaultSymbol.clone()
});

const map = new EsriMap({
  basemap: 'streets-navigation-vector'
});

const view = new MapView({
  center: START_COORDINATES,
  constraints: {
    minZoom: 14,
    snapToZoom: false
  },
  container: 'mapBlock',
  map,
  ui: {
    components: []
  },
  zoom: 16
});

const locateWidget = new LocateWidget({
  view
});

const featureLayer = new FeatureLayer({
  fields: [
    {
      name: 'id' as keyof IContainer,
      type: 'oid'
    },
    {
      name: 'containerNo' as keyof IContainer,
      type: 'string'
    },
    {
      name: 'missedPickUp' as keyof IContainer,
      type: 'string'
    },
    {
      name: 'capacity' as keyof IContainer,
      type: 'double'
    },
    {
      name: 'weight' as keyof IContainer,
      type: 'double'
    },
    {
      name: 'ratio' as keyof IContainer,
      type: 'double'
    },
    {
      name: 'company' as keyof IContainer,
      type: 'string'
    },
    {
      name: 'houseNo' as keyof IContainer,
      type: 'string'
    },
    {
      name: 'street' as keyof IContainer,
      type: 'string'
    },
    {
      name: 'location' as keyof IContainer,
      type: 'string'
    },
    {
      name: 'position' as keyof IContainer,
      type: 'geometry'
    },
    {
      name: 'history' as keyof IContainer,
      type: 'blob'
    },
    {
      name: 'lastUnload' as keyof IContainer,
      type: 'string'
    },
    {
      name: 'lastUnloadDays' as keyof IContainer,
      type: 'blob'
    },
    {
      name: 'lastUnloadWords' as keyof IContainer,
      type: 'string'
    }
  ],
  geometryType: 'point',
  objectIdField: 'id' as keyof IContainer,
  popupEnabled: true,
  popupTemplate: {
    content: (point: ICSVPoint<IContainer>) => {
      const a = point.graphic.attributes;
      const dataType = $('#container-data-type').val() as DataType;
      const dataByType: Record<DataType, () => void> = {
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
    outFields: ['*'],
    title: (point: ICSVPoint<IContainer>) => {
      return (
        point.graphic.attributes.containerNo +
        ' — ' +
        point.graphic.attributes.street +
        ' ' +
        point.graphic.attributes.houseNo
      );
    }
  },
  renderer: new SimpleRenderer(),
  source: [],
  spatialReference: {
    wkid: 4326
  },
  visible: true
});

map.layers.add(featureLayer, 1);

view.ui.add(locateWidget, 'top-left');
view.ui.add('legendBlock', 'bottom-left');

if (!PRODUCTION) {
  setupDeveloperMode();
}

function setupDeveloperMode() {
  view.on('click', clickEvent => {
    // tslint:disable-next-line: no-console
    console.debug('view', {
      clickEvent,
      latitude: clickEvent.mapPoint.latitude,
      longitude: clickEvent.mapPoint.longitude,
      scale: view.scale,
      view,
      zoom: view.zoom
    });
  });
}

export { view, map, featureLayer, locateWidget };
