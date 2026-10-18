// Replacement for `react-loading`, whose prebuilt bundle creates elements with a
// React 15/16 internal marker that React 19 rejects (minified error #525).
// Same props the app used: type (ignored, always a spinner), color, height, width.
import { CSSProperties } from 'react';
import { Spinner } from 'react-bootstrap';

interface Props { type?: string; color?: string; height?: number | string; width?: number | string; className?: string }

export default function Loading({ color, className }: Props) {
    // The old "bars" loader filled a box; a fixed-size spinner centred in it reads the same.
    const style: CSSProperties = { color, height: '4rem', width: '4rem', borderWidth: '0.3em' };
    return (
        <div className={'d-flex justify-content-center my-5 ' + (className || '')}>
            <Spinner animation="border" role="status" style={style} />
        </div>
    );
}
