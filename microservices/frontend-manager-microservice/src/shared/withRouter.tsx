// react-router v6+ removed withRouter; this keeps class components that read
// this.props.match.params / history / location working.
import { ComponentType } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';

export function withRouter<P>(Component: ComponentType<P & { match: { params: Record<string, string | undefined> } }>) {
  return function WithRouter(props: P) {
    const params = useParams();
    const navigate = useNavigate();
    const location = useLocation();
    const history = { push: (to: string) => navigate(to), replace: (to: string) => navigate(to, { replace: true }), goBack: () => navigate(-1) };
    return <Component {...props} match={{ params }} history={history} location={location} />;
  };
}
