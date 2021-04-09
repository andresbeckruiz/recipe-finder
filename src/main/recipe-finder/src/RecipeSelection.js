import {Link} from "react-router-dom";
import Button from "react-bootstrap/Button";
import { makeStyles } from '@material-ui/core/styles';
import {Card, CardActions, CardMedia, CardContent, Typography} from '@material-ui/core';

let recipes = [{recipeName:"Roast Chicken with Lemon and Garlic", chef: "Claire Saffitz", src: "https://assets.bonappetit.com/photos/5a8749c98e5ab504d767b208/16:9/w_2048,c_limit/no-fail-roast-chicken-with-lemon-and-garlic.jpg"},
    {recipeName:"Spicy Shrimp Pilaf", chef: "Andy Baraghani", src: "https://assets.bonappetit.com/photos/6048f5c9b5ad3ffa9fef04d7/1:1/w_2560%2Cc_limit/Comfort-Spicy-Shrimp-Pilaf.jpg"},
    {recipeName:"Granola Scones", chef: "Roxana Jullapat", src: "https://assets.bonappetit.com/photos/5ff4b06aa74c1e30caf0ee9b/1:1/w_2560%2Cc_limit/Mother-Grains-Granola-Scones.jpg"}]
let alt = false;

const useStyles = makeStyles({
    root: {
        minWidth: 275,
        backgroundColor: "#d9d9d9",
        marginLeft: 25,
        marginTop: 25,
        marginRight:25
    },
    title: {
        fontSize: 14,
    },
    pos: {
        marginBottom: 12,
    },
    media: {
        height: 175,
        width: 600
    },
});

function RecipeSelection() {

    // style details for root page
    const rootStyle = {
        backgroundColor: "white",
        height: '100vh'
    }

    // style details for list of recipes
    const style = {
        backgroundColor: "#2776ED",
        height: 600,
        width: 1300,
        position: "absolute",
        top: 150,
        left: 150
    }

    // style details for inner scroll div
    const innerStyle = {
        backgroundColor: "#FFF",
        height: 550,
        width: 1250,
        position: "relative",
        top: 25,
        left: 25,
        overflow: "scroll"
    }

    const classes = useStyles();




    return (
        <div style={rootStyle}>
            {/*dynamic header*/}
            <h1 style={{marginTop: 25}}>Select a Recipe</h1>
            {/*button on side of page*/}
            <Link to={"/Fridge"}>
                <Button variant="success" size= "lg" style={{position: "absolute", left: 50, top: 25}}>Back to Fridge</Button>
            </Link>
            <div style={style}>
                <div style={innerStyle}>
                    {recipes.map((r) => {
                        if (alt){
                            alt = false;
                            return <Card className={classes.root}>
                                <div style={{width: 600, float: "left"}}>
                                    <CardContent>
                                        <Typography variant="h5" component="h2">
                                            {/*recipe name*/}
                                            {r.recipeName}
                                        </Typography>
                                        <Typography variant="body2" component="p">
                                            <br/>
                                            {/*chef name*/}
                                            By: {r.chef}
                                        </Typography>
                                    </CardContent>
                                    <CardActions>
                                        <Link to={"/recipe"}>
                                        <Button size="small">See Recipe</Button>
                                        </Link>
                                    </CardActions>
                                </div>
                                <div style={{position: "relative", marginLeft: 650}}>
                                    <CardMedia
                                        className={classes.media}
                                        image={r.src}
                                        title={r.recipeName}
                                    />
                                </div>
                            </Card>
                        } else {
                            alt = true;
                            return <Card className={classes.root}>
                                <div style={{width: 600, float: "right"}}>
                                    <CardContent>
                                        <Typography style={{textAlign: "right"}} variant="h5" component="h2">
                                            {/*recipe name*/}
                                            {r.recipeName}
                                        </Typography>
                                        <Typography style={{textAlign: "right"}}variant="body2" component="p">
                                            <br/>
                                            {/*chef name*/}
                                            By: {r.chef}
                                        </Typography>
                                    </CardContent>
                                    <CardActions>
                                        <Link to={"/recipe"}>
                                            <Button style={{marginLeft:475}} size="small">See Recipe</Button>
                                        </Link>
                                    </CardActions>
                                </div>
                                <div style={{position: "relative", marginRight: 650}}>
                                    <CardMedia
                                        className={classes.media}
                                        image={r.src}
                                        title={r.recipeName}
                                    />
                                </div>
                            </Card>
                        }
                    }
                    )}
                </div>
            </div>
        </div>
    );
}

export default RecipeSelection;
