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
import { moment } from './moment-lt';

interface CSVContainerRow {
  Konteinerio_iraso_ID: number;
  Konteinerio_Nr: string;
  Konteinerio_pastatymo_data: number;
  Konteinerio_talpa: number;
  Konteinerio_vietove: string;
  Konteinerio_gatve: string;
  Konteinerio_namas: string;
  Konteinerio_butas: string;
  Konteinerio_RFID: string;
  'Konteinerio latitude': number;
  'Konteinerio longitude': number;
  Vezejas: string;
}

interface CSVPoint<T> {
  graphic: {
    attributes: T;
  };
}

export const init = () => {
  const map = new EsriMap({
    basemap: 'topo-vector'
  });

  const VGTU_COORDINATES = [25.33790900457582, 54.72238152593433];

  const csvRenderer = new SimpleRenderer({
    symbol: new SimpleMarkerSymbol({
      path:
        'M24.2784 7.29299C19.9829 3.06762 13.0185 3.06762 8.723 7.29299C7.70314 8.28937 6.89274 9.47956 6.33945 10.7936C5.78617 12.1077 5.50114 13.5191 5.50114 14.9449C5.50114 16.3707 5.78617 17.7821 6.33945 19.0961C6.89274 20.4102 7.70314 21.6004 8.723 22.5967L16.5 30.2486L24.2784 22.5967C25.2982 21.6004 26.1086 20.4102 26.6619 19.0961C27.2152 17.7821 27.5002 16.3707 27.5002 14.9449C27.5002 13.5191 27.2152 12.1077 26.6619 10.7936C26.1086 9.47956 25.2982 8.28937 24.2784 7.29299ZM16.5 18.5611C15.5815 18.5611 14.7194 18.2036 14.069 17.5546C13.4252 16.9094 13.0636 16.0351 13.0636 15.1236C13.0636 14.2121 13.4252 13.3378 14.069 12.6926C14.718 12.0436 15.5815 11.6861 16.5 11.6861C17.4185 11.6861 18.282 12.0436 18.931 12.6926C19.5748 13.3378 19.9364 14.2121 19.9364 15.1236C19.9364 16.0351 19.5748 16.9094 18.931 17.5546C18.282 18.2036 17.4185 18.5611 16.5 18.5611Z',
      outline: {
        color: 'white',
        width: 0.5
      }
    }),
    visualVariables: [
      new ColorVariable({
        field: 'Konteinerio_talpa',
        stops: [
          {
            value: 0,
            color: '#FF4262',
            label: '0 m³'
          },
          {
            value: 0.5,
            color: '#FDC010',
            label: '0.5 m³'
          },
          {
            value: 4.5,
            label: '4.5 m³',
            color: '#43A047'
          }
        ]
      })
    ]
  });

  const csvLayer = new CSVLayer({
    url: '/app/assets/Konteineriu sarasas.csv',
    latitudeField: 'Konteinerio latitude',
    longitudeField: 'Konteinerio longitude',
    // minScale: 20000,
    popupEnabled: true,
    renderer: csvRenderer,
    title: 'Vilniaus konteineriai',
    popupTemplate: {
      title: (point: CSVPoint<CSVContainerRow>) => {
        return point.graphic.attributes.Konteinerio_Nr;
      },
      content: (point: CSVPoint<CSVContainerRow>) => {
        return `
          <table>
            <tr><td>Gatvė</td><td>${point.graphic.attributes.Konteinerio_gatve}</td></tr>
            <tr><td>Namas</td><td>${point.graphic.attributes.Konteinerio_namas}</td></tr>
            ${
              point.graphic.attributes.Konteinerio_butas
                ? `<tr><td>Butas</td><td>${point.graphic.attributes.Konteinerio_butas || 'Nėra'}</td></tr>`
                : ''
            }
            <tr><td>Vietovė</td><td>${point.graphic.attributes.Konteinerio_vietove}</td></tr>
            <tr><td>Įrašo ID</td><td>${point.graphic.attributes.Konteinerio_iraso_ID}</td></tr>
            <tr><td>Pastatymo data</td><td>${moment(point.graphic.attributes.Konteinerio_pastatymo_data).format(
              'YYYY-MM-DD'
            )} </td></tr>
            <tr><td>Talpa</td><td>${point.graphic.attributes.Konteinerio_talpa} m³</td></tr>
            <tr><td>Vežėjas</td><td>${point.graphic.attributes.Vezejas}</td></tr>
          </table>`;
      },
      outFields: ['*']
    }
  });

  map.layers.add(csvLayer, 1);

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
    view

    // layerInfos: [
    //   {
    //     layer: csvLayer
    //   }
    // ]
  });

  view.ui.add(locateWidget, 'top-right');
  // view.ui.add(legendWidget, 'top-right');

  view.on('click', e => {
    console.log('view', {
      zoom: view.zoom,
      scale: view.scale,
      latitude: e.mapPoint.latitude,
      longitude: e.mapPoint.longitude,
      layer: csvLayer
    });
  });

  const overlayElement = document.querySelector('.overlay');
  const buttonFindLocation = document.getElementById('btn-find-location');
  const buttonEnterAddress = document.getElementById('btn-enter-address');
  const buttonShowMap = document.getElementById('btn-show-map');

  buttonFindLocation &&
    buttonFindLocation.addEventListener('click', e => {
      e.preventDefault();
      locateWidget.locate().then(() => {
        overlayElement && overlayElement.classList.add('hide');
      });
    });

  buttonEnterAddress &&
    buttonEnterAddress.addEventListener('click', e => {
      e.preventDefault();

      overlayElement && overlayElement.classList.add('hide');
    });

  buttonShowMap &&
    buttonShowMap.addEventListener('click', e => {
      e.preventDefault();

      overlayElement && overlayElement.classList.add('hide');
    });
};
