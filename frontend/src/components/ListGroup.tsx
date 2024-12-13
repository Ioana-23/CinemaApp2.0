import { Fragment} from "react";

function ListGroup() {
    let items = [
        'New York',
        'San Francisco'
    ];

    return (
        <>
            <h1>List</h1>
            {items.length === 0 ? <p>No items found!</p> : null}
            <ul className="list-group">
                {items.map(item =>
                    <li
                        className="list-group-item"
                        key={item}
                        onClick={() => console.log('Clicked')}
                    >
                        {item}
                    </li>)}
            </ul>
        </>
    );
}

export default ListGroup