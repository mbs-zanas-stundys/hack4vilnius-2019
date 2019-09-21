import EsriMap from 'esri/Map';
import MapView from 'esri/views/MapView';
import FeatureLayer from 'esri/layers/FeatureLayer';
import TileLayer from 'esri/layers/TileLayer';
import GeoJSONLayer from 'esri/layers/GeoJSONLayer';
import MapImageLayer from 'esri/layers/MapImageLayer';
import CSVLayer from 'esri/layers/CSVLayer';
import LocateWidget from 'esri/widgets/Locate';
import LegendWidget from 'esri/widgets/Legend';
import SimpleRenderer from 'esri/renderers/SimpleRenderer';
import UniqueValueRenderer from 'esri/renderers/UniqueValueRenderer';
import ClassBreaksRenderer from 'esri/renderers/ClassBreaksRenderer';
import SimpleMarkerSymbol from 'esri/symbols/SimpleMarkerSymbol';
import PictureMarkerSymbol from 'esri/symbols/PictureMarkerSymbol';
import ColorVariable from 'esri/renderers/visualVariables/ColorVariable';
import Graphic from 'esri/Graphic';
import PointGeometry from 'esri/geometry/Point';
import { moment } from './moment-lt';
import { Container, CSVPoint } from './types';
import { defaultSymbol, LEGEND_COLORS, VGTU_COORDINATES } from './constants';

const map = new EsriMap({
  basemap: 'topo-vector'
});

const renderer = new UniqueValueRenderer({
  uniqueValueInfos: [],
  defaultSymbol: defaultSymbol,
  authoringInfo: {},
  visualVariables: [
    new ColorVariable({
      field: 'capacity' as keyof Container,
      legendOptions: {
        title: 'Paskutinis išvežimas'
      },

      stops: [
        {
          value: 0,
          color: LEGEND_COLORS[3],
          label: '0 m³'
        },
        {
          value: 0.25,
          color: LEGEND_COLORS[2],
          label: '0 m³'
        },
        {
          value: 0.5,
          color: LEGEND_COLORS[1],
          label: '0.5 m³'
        },
        {
          value: 4.5,
          label: '4.5 m³',
          color: LEGEND_COLORS[0]
        }
      ]
    })
  ]
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
      console.log({ point });
      return `
        <table>
          <tr><td>Gatvė</td><td>${point.graphic.attributes.street}</td></tr>
          <tr><td>Namas</td><td>${point.graphic.attributes.houseNo}</td></tr>
          <tr><td>Vietovė</td><td>${point.graphic.attributes.location}</td></tr>
          <tr><td>Talpa</td><td>${point.graphic.attributes.capacity} m³</td></tr>
          <tr><td>Vežėjas</td><td>${point.graphic.attributes.company}</td></tr>
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
