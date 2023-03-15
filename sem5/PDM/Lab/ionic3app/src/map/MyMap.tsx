import {GoogleMap} from '@capacitor/google-maps';
import React, {useEffect, useRef} from 'react';
import {mapsApiKey} from "./mapsApiKey";

interface MyMapProps {
    lat: number;
    lng: number;
    onMapClick: (e: any) => void,
    onMarkerClick: (e: any) => void,
}

const MyMap: React.FC<MyMapProps> = ({lat, lng, onMapClick, onMarkerClick}) => {
    const mapRef = useRef<HTMLElement>(null);
    useEffect(myMapEffect, [mapRef.current])

    return (
        <div className="component-wrapper">
            <capacitor-google-map ref={mapRef} style={{
                display: 'block',
                width: 300,
                height: 400
            }}/>
        </div>
    );

    function myMapEffect() {
        let canceled = false;
        let googleMap: GoogleMap | null = null;
        createMap();
        return () => {
            canceled = true;
            googleMap?.removeAllMapListeners();
        }

        async function createMap() {
            if (!mapRef.current) {
                return;
            }
            googleMap = await GoogleMap.create({
                id: 'my-cool-map',
                element: mapRef.current,
                apiKey: mapsApiKey,
                config: {
                    center: {lat, lng},
                    zoom: 8
                }
            })
            let myLocationMarkerId = await googleMap.addMarker({coordinate: {lat, lng}, title: 'My location'});
            await googleMap.setOnMapClickListener(({latitude, longitude}) => {
                googleMap?.removeMarker(myLocationMarkerId)
                googleMap?.addMarker({
                    coordinate: {lat: latitude, lng: longitude},
                    title: 'My location'
                }).then(value => myLocationMarkerId = value)
                onMapClick({latitude, longitude})
            });
            await googleMap.setOnMarkerClickListener(({markerId, latitude, longitude}) => {
                onMarkerClick({markerId, latitude, longitude});
            });
        }
    }
}

export default MyMap;
