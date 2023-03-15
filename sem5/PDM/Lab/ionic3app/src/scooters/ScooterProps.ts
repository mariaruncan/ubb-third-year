export interface ScooterProps {
  _id?: string;
  number: number;
  locked: boolean;
  batteryLevel: number;
  userId?: string;
  latitude: number;
  longitude: number;
  photoPath: string;
}
