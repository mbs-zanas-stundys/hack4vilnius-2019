var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
define(["require", "exports", "esri/Map", "esri/views/MapView", "esri/layers/FeatureLayer", "esri/widgets/Locate", "esri/widgets/Legend", "esri/renderers/UniqueValueRenderer", "esri/symbols/SimpleMarkerSymbol", "esri/renderers/visualVariables/ColorVariable", "esri/Graphic", "esri/geometry/Point"], function (require, exports, Map_1, MapView_1, FeatureLayer_1, Locate_1, Legend_1, UniqueValueRenderer_1, SimpleMarkerSymbol_1, ColorVariable_1, Graphic_1, Point_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    Map_1 = __importDefault(Map_1);
    MapView_1 = __importDefault(MapView_1);
    FeatureLayer_1 = __importDefault(FeatureLayer_1);
    Locate_1 = __importDefault(Locate_1);
    Legend_1 = __importDefault(Legend_1);
    UniqueValueRenderer_1 = __importDefault(UniqueValueRenderer_1);
    SimpleMarkerSymbol_1 = __importDefault(SimpleMarkerSymbol_1);
    ColorVariable_1 = __importDefault(ColorVariable_1);
    Graphic_1 = __importDefault(Graphic_1);
    Point_1 = __importDefault(Point_1);
    function debounce(func, wait, immediate) {
        var timeout;
        return function () {
            var context = this;
            var args = arguments;
            var later = function () {
                timeout = null;
                if (!immediate)
                    func.apply(context, args);
            };
            var callNow = immediate && !timeout;
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
            if (callNow)
                func.apply(context, args);
        };
    }
    const SEARCH_RADIUS = 500;
    const get = (url, pathParams = {}, params = {}) => {
        const baseUrl = 'http://localhost:8080';
        let httpParams = Object.entries(params)
            .map(([key, value]) => `${key}=${value}`)
            .join('&');
        let constructedPath = url;
        Object.entries(pathParams).forEach(([key, value]) => (constructedPath = constructedPath.replace(`:${key}`, value)));
        if (httpParams) {
            httpParams = '?' + httpParams;
        }
        return fetch(baseUrl + constructedPath + httpParams).then(res => res.json());
    };
    const api = {
        containersByCoordinates: (lat, lon, distance) => get('/containers', undefined, { lat, lon, distance }),
        containersByAddress: (street, houseNo, flatNo) => get('/containers', undefined, { street, houseNo, flatNo }),
        containerSchedule: containerId => get('/container/:containerId', { containerId })
    };
    window.api = api;
    const PROD = false;
    const map = new Map_1.default({
        basemap: 'topo-vector'
    });
    const VGTU_COORDINATES = [25.33790900457582, 54.72238152593433];
    const renderer = new UniqueValueRenderer_1.default({
        uniqueValueInfos: [],
        defaultSymbol: new SimpleMarkerSymbol_1.default({
            size: 24,
            path: 'M24.2784 7.29299C19.9829 3.06762 13.0185 3.06762 8.723 7.29299C7.70314 8.28937 6.89274 9.47956 6.33945 10.7936C5.78617 12.1077 5.50114 13.5191 5.50114 14.9449C5.50114 16.3707 5.78617 17.7821 6.33945 19.0961C6.89274 20.4102 7.70314 21.6004 8.723 22.5967L16.5 30.2486L24.2784 22.5967C25.2982 21.6004 26.1086 20.4102 26.6619 19.0961C27.2152 17.7821 27.5002 16.3707 27.5002 14.9449C27.5002 13.5191 27.2152 12.1077 26.6619 10.7936C26.1086 9.47956 25.2982 8.28937 24.2784 7.29299ZM16.5 18.5611C15.5815 18.5611 14.7194 18.2036 14.069 17.5546C13.4252 16.9094 13.0636 16.0351 13.0636 15.1236C13.0636 14.2121 13.4252 13.3378 14.069 12.6926C14.718 12.0436 15.5815 11.6861 16.5 11.6861C17.4185 11.6861 18.282 12.0436 18.931 12.6926C19.5748 13.3378 19.9364 14.2121 19.9364 15.1236C19.9364 16.0351 19.5748 16.9094 18.931 17.5546C18.282 18.2036 17.4185 18.5611 16.5 18.5611Z',
            outline: {
                color: 'white',
                width: 0.5
            }
        }),
        authoringInfo: {},
        visualVariables: [
            new ColorVariable_1.default({
                field: 'capacity',
                legendOptions: {
                    title: 'Paskutinis išvežimas'
                },
                stops: [
                    {
                        value: 0,
                        color: '#FF4262',
                        label: '0 m³'
                    },
                    {
                        value: 0.25,
                        color: '#FF42FF',
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
    const view = new MapView_1.default({
        map: map,
        container: 'viewDiv',
        center: VGTU_COORDINATES,
        zoom: 16,
        constraints: {
            minZoom: 14,
            snapToZoom: false
        }
    });
    const locateWidget = new Locate_1.default({
        view
    });
    const legendWidget = new Legend_1.default({
        view,
        container: 'legendContentDiv'
        // layerInfos: [
        //   {
        //     layer: csvLayer
        //   }
        // ]
    });
    const featureLayer = new FeatureLayer_1.default({
        renderer,
        geometryType: 'point',
        visible: true,
        objectIdField: 'containerNo',
        source: [],
        fields: [
            {
                name: 'containerNo',
                type: 'oid'
            },
            {
                name: 'capacity',
                type: 'double'
            },
            {
                name: 'company',
                type: 'string'
            },
            {
                name: 'houseNo',
                type: 'string'
            },
            {
                name: 'street',
                type: 'string'
            },
            {
                name: 'location',
                type: 'string'
            },
            {
                name: 'position',
                type: 'geometry'
            }
        ],
        popupEnabled: true,
        spatialReference: {
            wkid: 4326
        },
        popupTemplate: {
            title: (point) => {
                return (point.graphic.attributes.containerNo +
                    ' — ' +
                    point.graphic.attributes.street +
                    ' ' +
                    point.graphic.attributes.houseNo);
            },
            content: (point) => {
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
    view.on('click', e => {
        console.log('view', {
            view,
            zoom: view.zoom,
            scale: view.scale,
            latitude: e.mapPoint.latitude,
            longitude: e.mapPoint.longitude
        });
    });
    (function initDomElements() {
        const overlayElement = $('.overlay');
        const actionsOverlay = $('.actions-overlay');
        const buttonFindLocation = $('#btn-find-location');
        const buttonEnterAddress = $('#btn-enter-address');
        const overlayButtons = $('.overlay-buttons');
        const overlayForm = $('.overlay form');
        const buttonShowMenu = $('#btn-show-menu');
        const buttonShowMap = $('#btn-show-map');
        const buttonShowButtons = $('#btn-show-buttons');
        const legendDiv = $('#legendDiv');
        const hideOverlay = () => {
            legendDiv.removeClass('hide');
            actionsOverlay.removeClass('hide');
            overlayElement.addClass('hide');
            setTimeout(() => {
                buttonFindLocation.removeClass('loading');
            }, 300);
        };
        const showOverlay = () => {
            legendDiv.addClass('hide');
            actionsOverlay.addClass('hide');
            overlayElement.removeClass('hide');
        };
        locateWidget.on('click', () => console.log('locateWidget'));
        const fetchFeaturesByCoords = (latitude, longitude) => {
            api
                .containersByCoordinates(latitude, longitude, Math.max(view.extent.height, view.extent.width, SEARCH_RADIUS) / 3.14)
                .then((containers) => {
                const features = containers.map(c => new Graphic_1.default({
                    geometry: new Point_1.default({
                        latitude: c.position.y,
                        longitude: c.position.x
                    }),
                    attributes: c
                }));
                featureLayer.queryFeatures().then(oldFeatures => {
                    featureLayer.applyEdits({
                        addFeatures: features,
                        deleteFeatures: oldFeatures.features
                    });
                });
                view.on('drag', () => {
                    debouncedFetchFeaturesByCoords(view.center.latitude, view.center.longitude);
                });
                view.on('mouse-wheel', () => {
                    debouncedFetchFeaturesByCoords(view.center.latitude, view.center.longitude);
                });
                view.on('resize ', () => {
                    debouncedFetchFeaturesByCoords(view.center.latitude, view.center.longitude);
                });
            });
        };
        const debouncedFetchFeaturesByCoords = debounce(fetchFeaturesByCoords, 300, false);
        buttonFindLocation.click(e => {
            e.preventDefault();
            buttonFindLocation.addClass('loading');
            locateWidget
                .locate()
                .then((coordinates) => fetchFeaturesByCoords(coordinates.coords.latitude, coordinates.coords.longitude))
                .then(() => hideOverlay())
                .catch(() => {
                console.log('Could not find location');
                hideOverlay();
            });
        });
        buttonEnterAddress.click(e => {
            e.preventDefault();
            overlayButtons.addClass('d-none');
            overlayForm.removeClass('d-none');
        });
        buttonShowMap.click(e => {
            e.preventDefault();
            fetchFeaturesByCoords(view.center.latitude, view.center.longitude);
            hideOverlay();
        });
        buttonShowMenu.click(e => {
            e.preventDefault();
            showOverlay();
        });
        buttonShowButtons.click(e => {
            e.preventDefault();
            overlayButtons.removeClass('d-none');
            overlayForm.addClass('d-none');
        });
        if (!PROD) {
            // buttonFindLocation.click();
        }
    })();
});
//# sourceMappingURL=main.js.map