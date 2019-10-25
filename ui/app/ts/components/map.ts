import FeatureLayer from 'esri/layers/FeatureLayer';
import EsriMap from 'esri/Map';
import { SimpleRenderer } from 'esri/renderers';
import UniqueValueRenderer from 'esri/renderers/UniqueValueRenderer';
import ColorVariable from 'esri/renderers/visualVariables/ColorVariable';
import MapView from 'esri/views/MapView';
import LocateWidget from 'esri/widgets/Locate';
import moment from 'moment';
import { COLORS, DEFAULT_SYMBOL, DOM, FORMAT, START_COORDINATES } from '../shared/constants';
import { T } from '../shared/translations';
import {
  DataType,
  IContainer,
  IContainerForDateDTO,
  IContainerForMap,
  IContainerPickupHistoryDTO,
  ICSVPoint,
  Unit
} from '../shared/types';

export const unloadRenderer = new UniqueValueRenderer({
  defaultSymbol: DEFAULT_SYMBOL.clone(),
  field: 'ratio' as keyof IContainer,
  legendOptions: {
    title: T.lastUnload
  },
  visualVariables: [
    new ColorVariable({
      field: 'ratio' as keyof IContainer,
      legendOptions: {
        title: T.lastUnload
      },
      stops: [
        {
          color: COLORS.error,
          label: '<2.5 ' + Unit.KgsPerCubicMeter,
          value: 0
        },
        {
          color: COLORS.warning,
          label: '2.5 — 5.0 ' + Unit.KgsPerCubicMeter,
          value: 2.5
        },
        {
          color: COLORS.primary,
          label: '5 — 7.5 ' + Unit.KgsPerCubicMeter,
          value: 5
        },
        {
          color: COLORS.secondary,
          label: '7.5 - 10.0 ' + Unit.KgsPerCubicMeter,
          value: 7.5
        },
        {
          color: COLORS.secondary,
          label: '>10.0 ' + Unit.KgsPerCubicMeter,
          value: 10
        }
      ]
    })
  ]
});

export const missedPickUpRenderer = new UniqueValueRenderer({
  field: 'missedPickUp' as keyof IContainer,
  legendOptions: {
    title: T.missedUnloads
  },
  uniqueValueInfos: [
    {
      label: T.notMissed,
      symbol: DEFAULT_SYMBOL.clone().set('color', COLORS.primary),
      value: 'false'
    },
    {
      label: T.missed,
      symbol: DEFAULT_SYMBOL.clone().set('color', COLORS.error),
      value: 'true'
    },
    {
      label: T.unknown,
      symbol: DEFAULT_SYMBOL.clone().set('color', COLORS.grey),
      value: T.unknown
    }
  ]
});

const map = new EsriMap({
  basemap: 'streets-navigation-vector'
});

const view = new MapView({
  center: START_COORDINATES,
  constraints: {
    minZoom: 12,
    snapToZoom: false
  },
  container: DOM.mapId,
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
    content: (point: ICSVPoint<IContainerForMap>) => {
      const dataType = $(DOM.containerDataTypeIdSelector).val() as DataType;

      const dataByType: Record<DataType, () => void> = {
        [DataType.missedPickups]: () => {
          const a = point.graphic.attributes as IContainerForDateDTO;
          return `<container-history container-no="${a.containerNo}"></container-history>`;
        },
        [DataType.unloadRatio]: () => {
          const a = point.graphic.attributes as IContainerPickupHistoryDTO;

          return `
            <table>
              <tr><td>${T.lastUnloadShortDate}</td><td>${moment(a.date).format(
            FORMAT.dateShort
          )}</td></tr>
              <tr><td>${T.lastUnloadShortWeight}</td><td>${a.weight} ${Unit.Kilograms}</td></tr>
              <tr><td>${T.containerCapacity}</td><td>${a.capacity} ${Unit.CubicMeters}</td></tr>
              <tr><td>${T.unloadShortRatio}</td><td>${Math.round(a.ratio * 100) / 100} ${
            Unit.KgsPerCubicMeter
          }</td></tr>
            </table>
            <br/>
            <container-history container-no="${a.containerNo}"></container-history>
          `;
        }
      };
      return dataByType[dataType]();
    },
    outFields: ['*'],
    title: (point: ICSVPoint<IContainerForMap>) => {
      return point.graphic.attributes.containerNo;
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
view.ui.add(DOM.legendBlockId, 'bottom-left');

export { view, map, featureLayer, locateWidget };
